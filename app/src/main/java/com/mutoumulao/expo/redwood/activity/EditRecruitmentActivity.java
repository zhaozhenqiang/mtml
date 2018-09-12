package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GridImageAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.entity.ServiceCategoryEntity;
import com.mutoumulao.expo.redwood.entity.event.PublishRecruitmentEntity;
import com.mutoumulao.expo.redwood.fragment.MineFragment;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.FullyGridLayoutManager;

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
 * Created by lzy on 2018/8/2.
 * 编辑招聘，求职，服务， 等多个类似界面
 */

public class EditRecruitmentActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.btn_go)
    Button mBtnGo;
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_type_name)
    TextView mTvTypeName;
    @BindView(R.id.ll_type_name)
    LinearLayout mLlTypeName;
    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter mAdapter;

    private String mType;
    private List<String> imageList = new ArrayList<>();
    private ArrayList<ServiceCategoryEntity> options1Items = new ArrayList<>();
    private String mCategory_id;
    private String mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_recruitment);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mType = getIntent().getStringExtra(MineFragment.EXTECL_TYPE);
        if ("0".equals(mType)) {
            mTvTitle.setText("编辑服务");
            loadServiceData();
            mLlTypeName.setVisibility(View.VISIBLE);

        } else if ("1".equals(mType)) {
            mTvTitle.setText("编辑招聘");

        }else if ("2".equals(mType)) {
            mTvTitle.setText("编辑求职");
        }
            mBtnGo.setText("确认发布");
        PositionEntity entity = (PositionEntity) getIntent().getSerializableExtra("entity");
        if (entity != null) {
            mEtName.setText(entity.title);
            mEtRemark.setText(entity.content);
            mId = entity.id;
            if (!TextUtils.isEmpty(entity.category_name)) {
                mTvTypeName.setText(entity.category_name);
                mCategory_id = entity.category_id;

            }
            String images = entity.images;
            if(!TextUtils.isEmpty(images)){
                List<String> list = JSON.parseArray(images, String.class);
                for (int i = 0; i < list.size(); i++) {
                    imageList.add(list.get(i));

                }
            }
            mAdapter = new GridImageAdapter(this, onAddPicClickListener);
            FullyGridLayoutManager manager = new FullyGridLayoutManager(EditRecruitmentActivity.this, 3, GridLayoutManager.VERTICAL, false);
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
                                PictureSelector.create(EditRecruitmentActivity.this).externalPicturePreview(position, selectList);
                                break;
                            default:
                                break;
                        }
                    }
                }
            });

        }

        StringUtil.setSelectionEnd(mEtName);
        StringUtil.setSelectionEnd(mEtRemark);
    }

    private void loadServiceData() {
        MineFuncManager.getInstance().serviceCategory(new ResponseCallback<ServiceCategoryEntity>() {
            @Override
            public void onSuccess(List<ServiceCategoryEntity> list) {
                super.onSuccess(list);
                options1Items.addAll(list);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onFailure(Exception e) {
                super.onFailure(e);
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(EditRecruitmentActivity.this)
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

    @OnClick({R.id.btn_go, R.id.rl_back, R.id.ll_type_name})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                publish();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_type_name:
                if (options1Items.size() > 0) {
                    UIUtil.hideSoftInput(this,mLlTypeName);
                    showPickerView();
                }
                break;
            default:
                break;
        }
    }

    private void publish() {
        String title = mEtName.getText().toString().trim();
        String remark = mEtRemark.getText().toString().trim();

        String images = "";
        for (int i = 0; i < imageList.size(); i++) {
            if (i < imageList.size() - 1) {
                images = images + imageList.get(i) + ";";
            } else {
                images = images + imageList.get(i);
            }
        }

        if (TextUtils.isEmpty(title)) {
            UIUtil.toastShort(this, "请输入标题");
            return;
        }
        if ("0".equals(mType)) {
            if (TextUtils.isEmpty(mCategory_id)) {
                UIUtil.toastShort(this, "请选择服务类型");
                return;
            }
        }
        if (TextUtils.isEmpty(remark)) {
            UIUtil.toastShort(this, "请输入内容");
            return;
        }

        if ("0".equals(mType)) {
            MineFuncManager.getInstance().updataService(mId, title, remark, mCategory_id, images, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    finish();
                    EventBus.getDefault().post(new PublishRecruitmentEntity(mType));
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(EditRecruitmentActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(EditRecruitmentActivity.this);
                }
            });

        } else if ("1".equals(mType)) {
            MineFuncManager.getInstance().updataRecruitment(mId,title, remark, images, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    finish();
                    EventBus.getDefault().post(new PublishRecruitmentEntity(mType));
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(EditRecruitmentActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(EditRecruitmentActivity.this);
                }
            });
        }else if ("2".equals(mType)) {
            MineFuncManager.getInstance().updataJob(mId,title, remark, images, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    finish();
                    EventBus.getDefault().post(new PublishRecruitmentEntity(mType));
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(EditRecruitmentActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(EditRecruitmentActivity.this);
                }
            });
        }
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

            UIUtil.showProgressBar(this,"",false);
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
                            imageList.add( url);
                            mAdapter.setList(imageList);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            UIUtil.hideProgressBar(EditRecruitmentActivity.this);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).category_name;
                mTvTypeName.setText(text);
                mCategory_id = options1Items.get(options1).id;
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(13)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        List<String> list = new ArrayList<>();
        for (int i = 0; i < options1Items.size(); i++) {
            list.add(options1Items.get(i).category_name);
        }
        pvOptions.setPicker(list);
        pvOptions.show();
    }
}
