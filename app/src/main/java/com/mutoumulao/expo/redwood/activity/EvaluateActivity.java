package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mutoumulao.expo.redwood.entity.event.OrderEvent;
import com.mutoumulao.expo.redwood.logic.OrderManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.FullyGridLayoutManager;
import com.mutoumulao.expo.redwood.view.StarRatingView;

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
 * Created by lzy on 2018/8/22.
 */

public class EvaluateActivity extends BaseActivity {

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
    @BindView(R.id.iv_goods)
    ImageView mIvGoods;
    @BindView(R.id.star_desc)
    StarRatingView mStarDesc;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.star_store)
    StarRatingView mStarStore;
    @BindView(R.id.star_service)
    StarRatingView mStarService;
    @BindView(R.id.et_content)
    EditText mEtContent;

    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private GridImageAdapter mAdapter;
    private int maxSelectNum = 9;

    private String star_store;
    private String star_desc;
    private String star_service;
    private String mOrder_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvRight.setText("发布");
        mRlRight.setVisibility(View.VISIBLE);
        mTvTitle.setText("评价");
        mOrder_id = getIntent().getStringExtra("order_id");
        String order_avater = getIntent().getStringExtra("order_avater");

        new BitmapTools(this).display(mIvGoods,UrlConst.IMAGE_URL + order_avater);

        mStarStore.setOnRateChangeListener(new StarRatingView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                double v = Double.parseDouble(rate + "")/2;
                star_store = v + "";
            }
        });
        mStarDesc.setOnRateChangeListener(new StarRatingView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                double v = Double.parseDouble(rate + "")/2;
                star_desc = v + "";
            }
        });
        mStarService.setOnRateChangeListener(new StarRatingView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                double v = Double.parseDouble(rate + "")/2;
                star_service = v + "";
            }
        });

        mAdapter = new GridImageAdapter(this, onAddPicClickListener);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(EvaluateActivity.this, 3, GridLayoutManager.VERTICAL, false);
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
                            PictureSelector.create(EvaluateActivity.this).externalPicturePreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(EvaluateActivity.this)
                    .openGallery(ofImage())
                    .maxSelectNum(maxSelectNum)
                    .imageSpanCount(4)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .compress(true)// 是否压缩
//                    .compressGrade(Luban.with(this))// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)
                    .previewImage(false)
                    .isGif(true)// 是否显示gif图片
                    .selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };


    @OnClick({R.id.rl_back, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_right:
                publish();
                break;
            default:
                break;
        }
    }

    private void publish() {
        String content = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(star_store)) {
            UIUtil.toastShort(this, "请给店铺评分");
            return;
        }
        if (TextUtils.isEmpty(star_desc)) {
            UIUtil.toastShort(this, "请给描述评分");
            return;
        }
        if (TextUtils.isEmpty(star_service)) {
            UIUtil.toastShort(this, "请给物流评分");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            UIUtil.toastShort(this, "请给宝贝评价");
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
        OrderManager.getInstance().commentOrder(mOrder_id,content,
                star_desc,star_service,star_store,images,new ResponseCallback(){
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        EventBus.getDefault().post(new OrderEvent());
                        finish();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        UIUtil.showProgressBar(EvaluateActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        UIUtil.hideProgressBar(EvaluateActivity.this);
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
                            UIUtil.hideProgressBar(EvaluateActivity.this);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

}
