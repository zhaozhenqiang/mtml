package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
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
 * Created by lzy on 2018/8/24.
 */

public class GoodsDescActicity extends BaseActivity {

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
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.et_desc)
    EditText mEtDesc;

    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter mAdapter;
    private int maxSelectNum = 99;
    private ArrayList<String> imageList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_goods_desc);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String desc = getIntent().getStringExtra("desc");
        ArrayList<String> image = getIntent().getStringArrayListExtra("image");
        if(desc != null){
            mEtDesc.setText(desc);
        }
        if(image!=null){
            imageList.clear();
            imageList.addAll(image);
        }



        mTvRight.setText("完成");
        mRlRight.setVisibility(View.VISIBLE);
        mTvTitle.setText("宝贝描述");
        mAdapter = new GridImageAdapter(this, onAddPicClickListener);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(GoodsDescActicity.this, 3, GridLayoutManager.VERTICAL, false);
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
                            PictureSelector.create(GoodsDescActicity.this).externalPicturePreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        StringUtil.setSelectionEnd(mEtDesc);

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(GoodsDescActicity.this)
                    .openGallery(ofImage())
                    .maxSelectNum(maxSelectNum)
                    .imageSpanCount(4)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .compress(true)// 是否压缩
//                    .compressGrade(Luban.with(this))// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)
                    .previewImage(false)
                    .isGif(false)// 是否显示gif图片
                    //.selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };


    @OnClick({R.id.rl_back, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                UIUtil.showConfirm(this, "返回将清空数据，是否确定？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageList.clear();
                        mEtDesc.setText("");
                        Intent intent = new Intent();
                        intent.putExtra("desc","");
                        intent.putStringArrayListExtra("image",imageList);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });

                break;
            case R.id.rl_right:
                toComplete();
                break;
            default:
                break;
        }
    }

    private void toComplete() {
        String desc = mEtDesc.getText().toString().trim();
        if(TextUtils.isEmpty(desc)){
            UIUtil.toastShort(this,"请填写宝贝描述");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("desc",desc);
        intent.putStringArrayListExtra("image",imageList);
        setResult(RESULT_OK,intent);
        finish();
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
        List<LocalMedia> newList = new ArrayList<>();

        //selectList = PictureSelector.obtainMultipleResult(data);
        newList = PictureSelector.obtainMultipleResult(data);
        selectList.addAll(newList);
        for (int i = 0; i < newList.size(); i++) {

            UIUtil.showProgressBar(this, "", false);
            File file = new File(newList.get(i).getCompressPath());
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
                            UIUtil.hideProgressBar(GoodsDescActicity.this);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }
}
