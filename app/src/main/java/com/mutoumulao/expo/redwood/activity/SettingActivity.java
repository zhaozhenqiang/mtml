package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.LoginEntity;
import com.mutoumulao.expo.redwood.entity.event.AccountChangeEvent;
import com.mutoumulao.expo.redwood.hx.DemoHelper;
import com.mutoumulao.expo.redwood.logic.LoginManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.FileUtil;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * Created by lzy on 2018/8/24.
 */

public class SettingActivity extends BaseActivity {

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
    @BindView(R.id.ll_reset_pwd)
    LinearLayout mLlResetPwd;
    @BindView(R.id.iv_head)
    CircleImageView mIvHead;
    @BindView(R.id.ll_avater)
    LinearLayout mLlAvater;
    @BindView(R.id.tv_username)
    EditText mTvUsername;
    @BindView(R.id.ll_username)
    LinearLayout mLlUsername;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.ll_address)
    LinearLayout mLlAddress;
    @BindView(R.id.ll_back_message)
    LinearLayout mLlBackMessage;
    @BindView(R.id.ll_clear_cook)
    LinearLayout mLlClearCook;
    @BindView(R.id.ll_guide)
    LinearLayout mLlGuide;
    @BindView(R.id.btn_go)
    Button mBtnGo;

    private int maxSelectNum = 1;
    private List<LocalMedia> selectList = new ArrayList<>();
    private BitmapTools mBitmapTools;
    private String mAvatar = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mBitmapTools = new BitmapTools(this);
        initView();

        loadData();
    }

    //获取个人信息
    private void loadData() {
        LoginManager.getInstance().getUserInfo(new ResponseCallback<LoginEntity>() {
            @Override
            public void onSuccess(LoginEntity loginEntity) {
                super.onSuccess(loginEntity);
                mAvatar = loginEntity.avatar;
                mBitmapTools.display(mIvHead, UrlConst.IMAGE_URL + mAvatar);
                mTvUsername.setText(loginEntity.nickname);
                StringUtil.setSelectionEnd(mTvUsername);
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(SettingActivity.this, "", false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(SettingActivity.this);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }

    private void initView() {
        mTvTitle.setText("设置");
        mBtnGo.setText("退出登录");
  /*      BitmapTools bitmapTools = new BitmapTools(this);
        SharedPutils sp = SharedPutils.getPreferences(this);

        mTvUsername.setText(TextUtils.isEmpty(sp.getUserName()) ? "" : sp.getUserName());
        bitmapTools.display(mIvHead, TextUtils.isEmpty(sp.getAvatar()) ? "" : UrlConst.IMAGE_URL + sp.getAvatar());*/
        mTvUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updataUserInfo();

                }
                return false;
            }
        });


    }

    @OnClick({R.id.rl_back, R.id.ll_reset_pwd, R.id.ll_avater, R.id.ll_address, R.id.ll_back_message, R.id.ll_clear_cook, R.id.ll_guide, R.id.btn_go})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_reset_pwd:
                intent = new Intent(this, RegisterActivity.class);
                ;
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.ll_avater:
                onAddPicClick();
                break;

            case R.id.ll_address:
                intent = new Intent(this, AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_back_message:
                intent = new Intent(this,BackMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_clear_cook:
                UIUtil.showConfirm(this, "是否清除应用缓存？", new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mBitmapTools.clearDiskCache(new ResponseCallback<String>() {
                            @Override
                            public void onSuccess(String s) {
                                super.onSuccess(s);
                                UIUtil.toastShort(SettingActivity.this, s);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                getDiskCache();
                            }
                        });
                    }
                });
                break;
            case R.id.ll_guide:
                intent = new Intent(this,UserGuideActivity.class);
                startActivity(intent );
                break;
            case R.id.btn_go:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        UIUtil.showConfirm(this, "确定退出登录吗？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPutils.getPreferences(SettingActivity.this).clearData();
                DemoHelper.getInstance().logout(false,null);
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                EventBus.getDefault().post(new AccountChangeEvent(false));
                finish();
            }
        });
    }

    private void updataUserInfo() {
        String name = mTvUsername.getText().toString().trim();

        LoginManager.getInstance().updataUserInfo(name,mAvatar,new ResponseCallback<LoginEntity>(){
            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }

    private void onAddPicClick() {
        PictureSelector.create(SettingActivity.this)
                .openGallery(ofImage())
                .maxSelectNum(maxSelectNum)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .compress(true)// 是否压缩
                .sizeMultiplier(0.5f)
                .synOrAsy(true)
                .isCamera(true)
                .previewImage(false)
                .isGif(false)// 是否显示gif图片
                .enableCrop(true)// 是否裁剪
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调
                sendPhoto(data);
                break;

            default:
                break;
        }

    }

    private void sendPhoto(Intent data) {

        selectList = PictureSelector.obtainMultipleResult(data);

        UIUtil.showProgressBar(this, "", false);
        File file = new File(selectList.get(0).getCompressPath());
        OkHttpClient mOkHttpClent = new OkHttpClient();
        mOkHttpClent.dispatcher().setMaxRequestsPerHost(9);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file));

        RequestBody requestBody = builder.build();

        CacheControl cacheControl = new CacheControl.Builder()
                .noStore()
                .build();

        Request request = new Request.Builder()
                .url(UrlConst.POST_IMAGE_UPLOAD)
                .post(requestBody)
                .cacheControl(cacheControl)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String s = response.body().string();
                    String code = JSONObject.parseObject(s).getString("code");
                    if ("200".equals(code)) {
                        String data = JSONObject.parseObject(s).getString("data");
                        mAvatar = JSONObject.parseObject(data).getString("url");
                        updataUserInfo();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBitmapTools.display(mIvHead, UrlConst.IMAGE_URL + mAvatar);
                            }
                        });
                        UIUtil.hideProgressBar(SettingActivity.this);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void getDiskCache() {
        new AsyncTask<Void, Void, Float>() {
            @Override
            protected Float doInBackground(Void... params) {
                File filePath = mBitmapTools.getDiskCachePath();
                return (float) FileUtil.getFileSize(filePath) / 1024 / 1024;
            }

            @Override
            protected void onPostExecute(Float result) {

            }
        }.execute();
    }
}
