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

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GridImageAdapter;
import com.mutoumulao.expo.redwood.adapter.OnlyDisplayGridImageAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.JsonEntity;
import com.mutoumulao.expo.redwood.entity.LoginEntity;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
import com.mutoumulao.expo.redwood.logic.LoginManager;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.JsonFileReader;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.CircleImageView;
import com.mutoumulao.expo.redwood.view.FullyGridLayoutManager;

import org.json.JSONArray;

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
 * Created by lzy on 2018/8/26.
 */

public class CreateStoreActivity extends BaseActivity {

    public final static int CHOOSE_SOME = 111;

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
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.iv_head)
    CircleImageView mIvHead;
    @BindView(R.id.ll_avater)
    LinearLayout mLlAvater;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.ll_username)
    LinearLayout mLlUsername;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.ll_address)
    LinearLayout mLlAddress;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_info)
    EditText mEtInfo;
    @BindView(R.id.rv_phone)
    RecyclerView mRvPhoto;
    @BindView(R.id.btn_go)
    Button mBtnGo;

    private boolean isEdit = false;
    private String avater = "";
    private String province = "";
    private String city = "";
    private String area = "";
    private int maxSelectNum = 1;
    private List<LocalMedia> selectList_av = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private BitmapTools mBitmapTools;
    private GridImageAdapter mAdapter;
    private List<String> imageList = new ArrayList<>();
    private ArrayList<JsonEntity> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private String mStore_id;
    private SharedPutils mPutils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store_info);
        ButterKnife.bind(this);
        initView();
        initJsonData();
    }

    private void initView() {
        mPutils = SharedPutils.getPreferences(this);
        mBitmapTools = new BitmapTools(this);
        mTvTitle.setText("商家信息");
        mBtnGo.setText("提交");
        isEdit = getIntent().getBooleanExtra("is_edit", false);


        if (isEdit) {
            loadData();
        } else {
            if ("0".equals(mPutils.getIsCheck())) {
                loadUser("0");
                mBtnGo.setVisibility(View.VISIBLE);
                mBtnGo.setText("等待审核");
                mBtnGo.setEnabled(false);
                mEtPhone.setEnabled(false);
                mEtInfo.setEnabled(false);
                mEtAddress.setEnabled(false);
                mEtName.setEnabled(false);
                mLlAvater.setEnabled(false);
                mLlAddress.setEnabled(false);
            } else if ("1".equals(mPutils.getIsCheck())) {
                loadUser("1");
                mBtnGo.setVisibility(View.VISIBLE);
                mBtnGo.setText("已通过审核");
                mBtnGo.setEnabled(false);
                mEtPhone.setEnabled(false);
                mEtInfo.setEnabled(false);
                mEtAddress.setEnabled(false);
                mEtName.setEnabled(false);
                mLlAvater.setEnabled(false);
                mLlAddress.setEnabled(false);
            } else if ("2".equals(mPutils.getIsCheck())) {
                loadUser("2");
                mBtnGo.setVisibility(View.VISIBLE);
                mBtnGo.setText("重新提交");
                mBtnGo.setEnabled(true);
                mAdapter = new GridImageAdapter(this, onAddPicClickListener);
                FullyGridLayoutManager manager = new FullyGridLayoutManager(CreateStoreActivity.this, 3, GridLayoutManager.VERTICAL, false);
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
                                    PictureSelector.create(CreateStoreActivity.this).externalPicturePreview(position, selectList);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
            } else {
                mAdapter = new GridImageAdapter(this, onAddPicClickListener);
                FullyGridLayoutManager manager = new FullyGridLayoutManager(CreateStoreActivity.this, 3, GridLayoutManager.VERTICAL, false);
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
                                    PictureSelector.create(CreateStoreActivity.this).externalPicturePreview(position, selectList);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
            }
        }
    }

    private void loadData() {
        MineFuncManager.getInstance().geStoreDetial(new ResponseCallback<StoreEntity>() {


            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(CreateStoreActivity.this);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(CreateStoreActivity.this);
            }

            @Override
            public void onSuccess(StoreEntity storeEntity) {
                super.onSuccess(storeEntity);
                if (storeEntity != null) {

                    if (!TextUtils.isEmpty(storeEntity.avatar)) {
                        avater = storeEntity.avatar;
                        mBitmapTools.display(mIvHead, UrlConst.IMAGE_URL + storeEntity.avatar);
                    }

                    mEtAddress.setText(storeEntity.address);
                    province = storeEntity.province_name;
                    city = storeEntity.city_name;
                    area = storeEntity.area_name;
                    mTvLocation.setText(province + city + area);
                    mEtPhone.setText(storeEntity.phone);
                    mStore_id = storeEntity.id;
                    mEtName.setText(storeEntity.business_name);
                    mEtInfo.setText(storeEntity.description);
                    String business_license = storeEntity.business_license;
//                    List<String> list = JSON.parseArray(business_license, String.class);
                    imageList.add(business_license);
                    FullyGridLayoutManager manager = new FullyGridLayoutManager(CreateStoreActivity.this, 3, GridLayoutManager.VERTICAL, false);
                    mRvPhoto.setLayoutManager(manager);
                    OnlyDisplayGridImageAdapter adapter = new OnlyDisplayGridImageAdapter(CreateStoreActivity.this, imageList);
                    mRvPhoto.setAdapter(adapter);


                    StringUtil.setSelectionEnd(mEtName);
                    StringUtil.setSelectionEnd(mEtPhone);
                    StringUtil.setSelectionEnd(mEtAddress);
                    StringUtil.setSelectionEnd(mEtInfo);
                }


            }
        });
    }

    @OnClick({R.id.rl_back, R.id.ll_avater, R.id.ll_address, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_avater:
                UIUtil.hideSoftInput(this,mLlAvater);
                onAddPicClick();
                break;
            case R.id.ll_address:
                UIUtil.hideSoftInput(this,mLlAddress);
                showPickerView();
                break;
            case R.id.btn_go:
                publish();
                break;
            default:
                break;
        }
    }

    private void publish() {
        String name = mEtName.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
        String info = mEtInfo.getText().toString().trim();
        String phone = mEtPhone.getText().toString().trim();
        String location = mTvLocation.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            UIUtil.toastShort(this, "请输入厂家名称");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            UIUtil.toastShort(this, "请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(info)) {
            UIUtil.toastShort(this, "请输入介绍详情");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            UIUtil.toastShort(this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(location)) {
            UIUtil.toastShort(this, "请选择地区");
            return;
        }
        if (TextUtils.isEmpty(avater)) {
            UIUtil.toastShort(this, "请选择头像");
            return;
        }
        if (TextUtils.isEmpty(province)) {
            UIUtil.toastShort(this, "请选择省市区");
        }
        String photo = "";
        for (int i = 0; i < imageList.size(); i++) {
            if (i < imageList.size() - 1) {
                photo = photo + imageList.get(i) + ";";
            } else {
                photo = photo + imageList.get(i);
            }
        }
/*        if (TextUtils.isEmpty(photo)) {
            UIUtil.toastShort(this, "请选择营业执照或身份证照片");
            return;
        }*/

        if (!isEdit) {
            if ("2".equals(mPutils.getIsCheck())) {
                MineFuncManager.getInstance().geStoreReAuth(mStore_id, name, avater, phone, province, city, area, address, info, photo, new ResponseCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        finish();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        UIUtil.showProgressBar(CreateStoreActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        UIUtil.hideProgressBar(CreateStoreActivity.this);
                    }
                });

            } else {
                MineFuncManager.getInstance().geStoreAuth(name, avater, phone, province, city, area, address, info, photo, new ResponseCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        finish();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        UIUtil.showProgressBar(CreateStoreActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        UIUtil.hideProgressBar(CreateStoreActivity.this);
                    }
                });
            }
        } else {
            MineFuncManager.getInstance().storeupdate(mStore_id, name, avater, phone, province, city, area, address, info, photo, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    finish();
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(CreateStoreActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(CreateStoreActivity.this);
                }
            });

        }
    }


    //获取个人信息
    private void loadUser(final String check) {
        LoginManager.getInstance().getUserInfo(new ResponseCallback<LoginEntity>() {
            @Override
            public void onSuccess(LoginEntity loginEntity) {
                super.onSuccess(loginEntity);
                if (loginEntity != null) {

                    if (loginEntity.business != null) {
                        avater = loginEntity.business.avatar;
                        mBitmapTools.display(mIvHead, UrlConst.IMAGE_URL + loginEntity.business.avatar);
                        mEtAddress.setText(loginEntity.business.address);
                        province = loginEntity.business.province_name;
                        city = loginEntity.business.city_name;
                        area = loginEntity.business.area_name;
                        mTvLocation.setText(province + city + area);
                        mEtPhone.setText(loginEntity.business.phone);
                        mEtName.setText(loginEntity.business.business_name);
                        mEtInfo.setText(loginEntity.business.description);
                        String business_license = loginEntity.business.business_license;
                        imageList.add(business_license);
                        mStore_id = loginEntity.business.id;
                        if (!"2".equals(check)) {
                            FullyGridLayoutManager manager = new FullyGridLayoutManager(CreateStoreActivity.this, 3, GridLayoutManager.VERTICAL, false);
                            mRvPhoto.setLayoutManager(manager);
                            OnlyDisplayGridImageAdapter adapter = new OnlyDisplayGridImageAdapter(CreateStoreActivity.this, imageList);
                            mRvPhoto.setAdapter(adapter);
                        }
                    }


                }
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(CreateStoreActivity.this, "", false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(CreateStoreActivity.this);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }


    private void onAddPicClick() {
        PictureSelector.create(this)
                .openGallery(ofImage())
                .maxSelectNum(1)
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
                .selectionMedia(selectList_av)// 是否传入已选图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(CreateStoreActivity.this)
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
                    .forResult(CHOOSE_SOME);//结果回调onActivityResult code
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调 头像
                sendAvaterPhoto(data);
                break;

            case CHOOSE_SOME:
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
                            UIUtil.hideProgressBar(CreateStoreActivity.this);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    private void sendAvaterPhoto(Intent data) {

        selectList_av = PictureSelector.obtainMultipleResult(data);

        UIUtil.showProgressBar(this, "", false);
        File file = new File(selectList_av.get(0).getCompressPath());
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
                        avater = JSONObject.parseObject(data).getString("url");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBitmapTools.display(mIvHead, UrlConst.IMAGE_URL + avater);
                            }
                        });
                        UIUtil.hideProgressBar(CreateStoreActivity.this);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initJsonData() {   //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonEntity> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }


    public ArrayList<JsonEntity> parseData(String result) {//Gson 解析
        ArrayList<JsonEntity> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonEntity entity = gson.fromJson(data.optJSONObject(i).toString(), JsonEntity.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                mTvLocation.setText(text);

                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                area = options3Items.get(options1).get(options2).get(options3);

            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(13)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


}
