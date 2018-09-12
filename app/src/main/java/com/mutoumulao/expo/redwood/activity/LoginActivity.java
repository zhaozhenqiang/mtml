package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mutoumulao.expo.redwood.MainActivity;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.CodeEntity;
import com.mutoumulao.expo.redwood.entity.LoginEntity;
import com.mutoumulao.expo.redwood.entity.event.AccountChangeEvent;
import com.mutoumulao.expo.redwood.logic.LoginManager;
import com.mutoumulao.expo.redwood.util.CountDown;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.BaseResponse;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzy on 2018/7/29.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_code_login)
    TextView mTvCodeLogin;
    @BindView(R.id.tv_pwd_login)
    TextView mTvPwdLogin;
    @BindView(R.id.scrollbar)
    ImageView mScrollbar;
    @BindView(R.id.vp)
    ViewPager mVp;

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.rl_right)
    RelativeLayout mRlRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.tv_qq_login)
    ImageView mTvQqLogin;
    @BindView(R.id.tv_wechat_login)
    ImageView mTvWechatLogin;
    @BindView(R.id.tv_sina_login)
    ImageView mTvSinaLogin;
    private ArrayList<View> mPageview;

    private int bmpW;
    private int offset;
    private int one;
    private int currIndex;

    private UMShareAPI mShareAPI;
    private String oauth_type = "";
    private SharedPutils sp;
    private EditText mEt_phone_c;
    private TextView mTv_get_code_c;
    private CountDown countDown;
    private String mCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mShareAPI = UMShareAPI.get(this);
        sp = SharedPutils.getPreferences(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        View code_view = inflater.inflate(R.layout.layout_code_login, null);
        View pwd_view = inflater.inflate(R.layout.layout_pwd_login, null);

        /*验证码登录布局*/
        mEt_phone_c = code_view.findViewById(R.id.et_phone);
        mTv_get_code_c = code_view.findViewById(R.id.tv_get_code);
        final EditText et_vcode_c = code_view.findViewById(R.id.et_vcode);
        Button btn_go_c = code_view.findViewById(R.id.btn_go);

        /*密码登录布局*/
        final EditText et_phone_p = pwd_view.findViewById(R.id.et_phone);
        TextView tv_forget_p = pwd_view.findViewById(R.id.tv_forget_pwd);
        final EditText et_pwd_p = pwd_view.findViewById(R.id.et_pwd);
        Button btn_go_p = pwd_view.findViewById(R.id.btn_go);

        mTvCodeLogin.setOnClickListener(this);
        mTvPwdLogin.setOnClickListener(this);
        mRlRight.setOnClickListener(this);
        mRlBack.setOnClickListener(this);
        mTvQqLogin.setOnClickListener(this);
        mTvWechatLogin.setOnClickListener(this);
        mTvSinaLogin.setOnClickListener(this);
        mTvTitle.setText("");
        mTvRight.setText("注册");
        mRlRight.setVisibility(View.VISIBLE);
        mRlTitle.setBackgroundColor(getResources().getColor(R.color.white));

        mTv_get_code_c.setOnClickListener(this);


        btn_go_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                String code = et_vcode_c.getText().toString().trim();
                String phone = mEt_phone_c.getText().toString().trim();
                if (!StringUtil.isMobileNumber(phone)) {
                    UIUtil.toastShort(LoginActivity.this, "请输入正确的手机号码");
                    return;
                }
                if (mCode == null || !mCode.equals(code)) {
                    UIUtil.toastShort(LoginActivity.this, "请输入正确验证码");
                    return;
                }
                codeLogin(phone);
            }
        });
        // 此处一下部分为密码登录部分
        tv_forget_p.setOnClickListener(this);
        btn_go_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone_p.getText().toString().trim();
                String pwd = et_pwd_p.getText().toString().trim();
                if (!StringUtil.isMobileNumber(phone)) {
                    UIUtil.toastShort(LoginActivity.this, "请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    UIUtil.toastShort(LoginActivity.this, "请输入密码");
                    return;
                }
                pwdLogin(phone, pwd);
            }
        });


        mPageview = new ArrayList<>();
        mPageview.add(code_view);
        mPageview.add(pwd_view);

        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            //获取当前窗体界面数
            public int getCount() {
                return mPageview.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(mPageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(mPageview.get(arg1));
                return mPageview.get(arg1);
            }
        };

        //绑定适配器
        mVp.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        mVp.setCurrentItem(0);
        //添加切换界面的监听器
        mVp.addOnPageChangeListener(new MyOnPageChangeListener());

        // 获取滚动条的宽度
        bmpW = mScrollbar.getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        int screenWidth = DisplayUtil.getScreenWidth(this);
        offset = (screenWidth / 2 - bmpW) / 2;
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        mScrollbar.setImageMatrix(matrix);
    }

    private void pwdLogin(final String phone, String pwd) {
        LoginManager.getInstance().getLogin(phone, pwd, new ResponseCallback<LoginEntity>() {

           /* @Override
            public void onSuccess(LoginEntity loginEntity) {
                super.onSuccess(loginEntity);
                EMClient.getInstance().login(phone, "111111", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        try {
                            EMClient.getInstance().chatManager().loadAllConversations();
                            EMClient.getInstance().groupManager().loadAllGroups();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new AccountChangeEvent(true));
                                    UIUtil.hideProgressBar(LoginActivity.this);
                                    UIUtil.toastShort(LoginActivity.this, "登录成功");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        UIUtil.toastShort(LoginActivity.this, s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }*/

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(LoginActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }


            @Override
            public void onFailure(Exception e) {
                super.onFailure(e);
                UIUtil.toastShort(LoginActivity.this, e.toString());
                UIUtil.hideProgressBar(LoginActivity.this);

            }
        });
    }


    private void codeLogin(final String phone) {
        LoginManager.getInstance().getMessageLogin(phone, mCode, new ResponseCallback<LoginEntity>() {

            /* @Override
             public void onSuccess(LoginEntity loginEntity) {
                 super.onSuccess(loginEntity);
                 EMClient.getInstance().login(phone, "111111", new EMCallBack() {
                     @Override
                     public void onSuccess() {
                         try {
                             EMClient.getInstance().chatManager().loadAllConversations();
                             EMClient.getInstance().groupManager().loadAllGroups();
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     EventBus.getDefault().post(new AccountChangeEvent(true));
                                     UIUtil.hideProgressBar(LoginActivity.this);
                                     UIUtil.toastShort(LoginActivity.this, "登录成功");
                                 }
                             });
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }

                     @Override
                     public void onError(int i, String s) {
                         UIUtil.toastShort(LoginActivity.this, s);
                     }

                     @Override
                     public void onProgress(int i, String s) {
                     }
                 });
             }
 */
            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(LoginActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }


            @Override
            public void onFailure(Exception e) {
                super.onFailure(e);
                UIUtil.hideProgressBar(LoginActivity.this);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_code_login:
                mVp.setCurrentItem(0);
                break;
            case R.id.tv_pwd_login:
                mVp.setCurrentItem(1);
                break;

            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, RegisterActivity.class).putExtra("type", "3"));
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_right:
                startActivity(new Intent(this, RegisterActivity.class).putExtra("type", "2"));
                break;
            case R.id.tv_qq_login:
                oauth_type = "qq";
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.tv_wechat_login:
                oauth_type = "weixin";
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);

                break;
            case R.id.tv_sina_login:
                oauth_type = "weibo";
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
                break;

            default:
                break;
        }
    }

    private void getCode() {
        String phone_c = mEt_phone_c.getText().toString().trim();
        if (!StringUtil.isMobileNumber(phone_c)) {
            UIUtil.toastShort(LoginActivity.this, "请输入正确的手机号码");
            return;
        }
        LoginManager.getInstance().getSendMessage(phone_c, "1", new ResponseCallback<CodeEntity>() {
            @Override
            public void onSuccess(CodeEntity s) {
                super.onSuccess(s);

                mCode = s.code;
                countDown = new CountDown(mTv_get_code_c, 60000, 1000);
                countDown.start();

            }

            @Override
            public boolean isShowNotice() {
                return true;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(LoginActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(LoginActivity.this);
            }
        });
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = position;
            textColor(position);
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            mScrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void textColor(int pos) {
        if (0 == pos) {
            mTvCodeLogin.setTextColor(getResources().getColor(R.color.tc_red));
            mTvPwdLogin.setTextColor(getResources().getColor(R.color.tc_black6));
        } else if (1 == pos) {
            mTvCodeLogin.setTextColor(getResources().getColor(R.color.tc_black6));
            mTvPwdLogin.setTextColor(getResources().getColor(R.color.tc_red));
        } else {
            return;
        }
    }

    private void otherLogin(final String uid) {
        LoginManager.getInstance().getOtherLogin(uid, oauth_type, new ResponseCallback<LoginEntity>() {
            @Override
            public void onResponse(BaseResponse response) {
                super.onResponse(response);
            }


            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(LoginActivity.this);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(Exception e) {
                super.onFailure(e);
                if ("第三方登录没有绑定手机号无法登录".equals(e.getMessage())) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("type", "4");
                    intent.putExtra("uid", uid);
                    intent.putExtra("oauth_type", oauth_type);
                    startActivity(intent);
                    UIUtil.hideProgressBar(LoginActivity.this);
                }
                UIUtil.hideProgressBar(LoginActivity.this);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            String uid = "";
            if (platform == SHARE_MEDIA.WEIXIN) {
                if (!TextUtils.isEmpty(sp.getWxuid())) {
                    uid = sp.getWxuid();
                } else {
                    uid = data.get("unionid");
                    sp.setWxuid(uid);
                }

            } else if (platform == SHARE_MEDIA.QQ) {
                uid = data.get("unionid");
                sp.setqquid(uid);
            } else {
                uid = data.get("uid");
                sp.setWbuid(uid);
            }

            sp.setLogintype(oauth_type);
            try {
                if (!TextUtils.isEmpty(uid)) {
                    otherLogin(uid);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                ToastUtil.showToast(context, getString(R.string.server_error), Toast.LENGTH_SHORT);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

    // 如果用户登录成功了，退出登录界面
    @Subscribe
    public void onEvent(AccountChangeEvent event) {
        if (event.isLogin) {
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (countDown != null) {
            countDown.cancel();
        }
    }
}
