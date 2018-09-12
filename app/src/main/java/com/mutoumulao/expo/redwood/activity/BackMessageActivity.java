package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GridImageAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.FullyGridLayoutManager;

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
 * Created by lzy on 2018/8/28.
 */

public class BackMessageActivity extends BaseActivity {

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
    @BindView(R.id.et_message)
    EditText mEtMessage;
    @BindView(R.id.et_mail)
    EditText mEtMail;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.btn_go)
    Button mBtnGo;


    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter mAdapter;

    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_message);
        ButterKnife.bind(this);
        mTvTitle.setText("用户反馈");
        mTvRight.setText("完成");
        mRlRight.setVisibility(View.GONE);
        mBtnGo.setText("完成");

        mAdapter = new GridImageAdapter(this, onAddPicClickListener);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(BackMessageActivity.this, 3, GridLayoutManager.VERTICAL, false);
        mRvPhoto.setLayoutManager(manager);
        mRvPhoto.setAdapter(mAdapter);
        mAdapter.setList(imageList);
        mAdapter.setSelectMax(maxSelectNum);
        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(BackMessageActivity.this).externalPicturePreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });

    }

    @OnClick({R.id.rl_back, R.id.rl_right, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_right:
                publish();
                break;
            case R.id.btn_go:
                publish();
                break;
            default:
                break;

        }
    }

    private void publish() {
        String message = mEtMessage.getText().toString().trim();
        String mail = mEtMail.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            UIUtil.toastShort(this, "请输入您的意见");
            return;
        }

        if (!StringUtil.checkEmail(mail)) {
            UIUtil.toastShort(this, "请输入正确的邮箱");
            return;
        }

        String images = "";
        for (int i = 0; i < imageList.size(); i++) {
            if (i < imageList.size() - 1) {
                images = images + imageList.get(i) + ";";
            } else {
                images = images + imageList.get(i);
            }
        }

        MineFuncManager.getInstance().sendMessage(message, mail, images, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                finish();
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(BackMessageActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(BackMessageActivity.this);
            }
        });

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
        for (int i = 0; i < selectList.size(); i++) {

            UIUtil.showProgressBar(this, "", false);
            File file = new File(selectList.get(i).getCompressPath());
            OkHttpClient mOkHttpClent = new OkHttpClient();
            mOkHttpClent.dispatcher().setMaxRequestsPerHost(9);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("value", i + "")
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
                            String url = JSONObject.parseObject(data).getString("url");
                            imageList.add(url);
                            mAdapter.setList(imageList);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            UIUtil.hideProgressBar(BackMessageActivity.this);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(BackMessageActivity.this)
                    .openGallery(ofImage())
                    .maxSelectNum(maxSelectNum)
                    .imageSpanCount(4)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .compress(true)// 是否压缩
                    .sizeMultiplier(0.5f)
                    .synOrAsy(true)
//                    .compressGrade(Luban.with(this))// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)
                    .previewImage(false)
                    .isGif(false)// 是否显示gif图片
                    .selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

}
