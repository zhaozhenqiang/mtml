package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.CodeEntity;
import com.mutoumulao.expo.redwood.entity.LoginEntity;
import com.mutoumulao.expo.redwood.entity.event.AccountChangeEvent;
import com.mutoumulao.expo.redwood.logic.LoginManager;
import com.mutoumulao.expo.redwood.util.CountDown;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/7/29.
 */

public class RegisterActivity extends BaseActivity {

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
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_vcode)
    EditText mEtVcode;
    @BindView(R.id.tv_get_code)
    TextView mTvGetCode;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_go)
    Button mBtnGo;
    private String mType;

    private CountDown countDown;
    private String mCode;
    private String mUid;
    private String mOauth_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mType = getIntent().getStringExtra("type");
        if ("2".equals(mType)) {

            mBtnGo.setText("注册");
            mTvTitle.setText("注册");
        } else if ("3".equals(mType)) {
            mBtnGo.setText("确定");
            mTvTitle.setText("修改密码");
        } else if ("4".equals(mType)) {
            mBtnGo.setText("登录");
            mTvTitle.setText("绑定手机号");
            mEtPwd.setVisibility(View.GONE);
            mUid = getIntent().getStringExtra("uid");
            mOauth_type = getIntent().getStringExtra("oauth_type");
        }
        mRlTitle.setBackgroundColor(getResources().getColor(R.color.white));
    }


    @OnClick({R.id.rl_back, R.id.tv_get_code, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_get_code:
                String phone_c = mEtPhone.getText().toString().trim();
                if (!StringUtil.isMobileNumber(phone_c)) {
                    UIUtil.toastShort(RegisterActivity.this, "请输入正确的手机号码");
                    return;
                }

                LoginManager.getInstance().getSendMessage(phone_c, "4".equals(mType) ? "2" : mType ,new ResponseCallback<CodeEntity>() {
                @Override
                public void onSuccess(CodeEntity s) {
                    super.onSuccess(s);

                    mCode = s.code;
                    countDown = new CountDown(mTvGetCode, 60000, 1000);
                    countDown.start();
                }

                @Override
                public boolean isShowNotice() {
                    return true;
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(RegisterActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(RegisterActivity.this);
                }
            });

                break;
            case R.id.btn_go:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        final String phone = mEtPhone.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        String code = mEtVcode.getText().toString().trim();
        if (!StringUtil.isMobileNumber(phone)) {
            UIUtil.toastShort(RegisterActivity.this, "请输入正确的手机号码");
            return;
        }
        if (mCode == null || !mCode.equals(code)) {
            UIUtil.toastShort(RegisterActivity.this, "请输入正确验证码");
            return;
        }
        if ("2".equals(mType) || ("3".equals(mType))) {
            if (TextUtils.isEmpty(pwd)) {
                UIUtil.toastShort(RegisterActivity.this, "请输入密码");
                return;
            }
        }
        if ("2".equals(mType)) {
            LoginManager.getInstance().getRegister(phone, pwd, mCode, new ResponseCallback<LoginEntity>() {
/*
                @Override
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
                                        UIUtil.hideProgressBar(RegisterActivity.this);
                                        UIUtil.toastShort(RegisterActivity.this,"登录成功");
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            UIUtil.toastShort(RegisterActivity.this,s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }*/

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(RegisterActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                }


                @Override
                public void onFailure(Exception e) {
                    super.onFailure(e);
                    UIUtil.hideProgressBar(RegisterActivity.this);
                }
            });
        } else if ("3".equals(mType)) {
            LoginManager.getInstance().getForgetLogin(phone, pwd, mCode, new ResponseCallback() {

                @Override
                public void onSuccess(Object loginEntity) {
                    super.onSuccess(loginEntity);
                  /*  SharedPutils.getPreferences(RegisterActivity.this).clearData();
                    DemoHelper.getInstance().logout(false, null);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    EventBus.getDefault().post(new AccountChangeEvent(false));*/
                    finish();
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(RegisterActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(RegisterActivity.this);
                }
            });

        } else if ("4".equals(mType)) {
            LoginManager.getInstance().getUserBind(phone, mUid, mCode, mOauth_type, new ResponseCallback<LoginEntity>() {
 /*
                @Override
                public void onSuccess(LoginEntity loginEntity){
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
                                        UIUtil.hideProgressBar(RegisterActivity.this);
                                        UIUtil.toastShort(RegisterActivity.this,"登录成功");
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            UIUtil.toastShort(RegisterActivity.this,s);
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
                    UIUtil.showProgressBar(RegisterActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }

                @Override
                public void onFailure(Exception e) {
                    super.onFailure(e);
                    UIUtil.hideProgressBar(RegisterActivity.this);
                }
            });

        }
    }

    // 如果用户登录成功了，退出登录界面
    @Subscribe
    public void onEvent(AccountChangeEvent event) {
        if (event.isLogin) {
            /*startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();*/
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
