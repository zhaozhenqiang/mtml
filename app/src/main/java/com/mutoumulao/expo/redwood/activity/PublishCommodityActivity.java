package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
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
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GridImageAdapterNew;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.JsonEntity;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.event.CreateGoodsEventy;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.JsonFileReader;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
 * Created by lzy on 2018/8/4.
 * 商品规格
 */

public class PublishCommodityActivity extends BaseActivity {
    private static final int SELECT_SPEC = 102;
    private static final int SELECT_DESC = 103;

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.rl_right)
    RelativeLayout mRlRight;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.tv_type_name)
    TextView mTvTypeName;
    @BindView(R.id.ll_type_name)
    LinearLayout mLlTypeName;
    @BindView(R.id.tv_goods_spec)
    TextView mTvGoodsSpec;
    @BindView(R.id.ll_goods_spec)
    LinearLayout mLlGoodsSpec;
    @BindView(R.id.tv_transport_expense)
    EditText mEvTransportExpense;
    @BindView(R.id.ll_transport_expense)
    LinearLayout mLlTransportExpense;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.ll_desc)
    LinearLayout mLlDesc;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.ll_location)
    LinearLayout mLlLocation;
    @BindView(R.id.btn_go)
    Button mBtnGo;

    @BindView(R.id.ll_price)
    View mLlPrice;
    @BindView(R.id.ll_number)
    View mLlNumber;
    @BindView(R.id.et_number)
    EditText mEtNumber;
    @BindView(R.id.et_price)
    EditText mEtPrice;



    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapterNew mAdapter;
    private int maxSelectNum = 99;
    private List<JsonEntity> options1Items = new ArrayList<>();
    private List<ArrayList<String>> options2Items = new ArrayList<>();
    private List<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private List<StoreManagerListEntity.SkuListEntity> mGood_spec;

    private String[] furnitureArray1 = {"沙发", "床", "桌类", "椅凳", "柜类", "茶台", "架类", "家具", "工艺品"};
    private List<String> optionsList1 = new ArrayList<>();
    private String[] furnitureArray2 = {"非洲黄花梨", "刺猬紫檀", "鸡翅", "小叶红檀", "大果紫檀", "檀香紫檀",
            "安达曼紫檀", "印度紫檀", "囊状紫檀", "中美洲黄檀", "刀状黑黄檀", "阔叶紫檀", "卢氏黑黄檀"
            , "东非黑黄檀", "巴西黑黄檀", "亚马逊黄檀", "伯利兹黄檀", "巴里黄檀", "赛州黄檀", "交趾黄檀"
            , "绒毛黄檀", "奥氏黄檀", "微凹黄檀", "苏拉威西乌木", "菲律宾乌木", "厚瓣乌木", "乌木", "非洲崖豆木"
            , "白花崖豆木", "铁木刀"};
    private List<String> optionsList2 = new ArrayList<>();
    private String[] furnitureArray3 = {"光身", "成品"};
    private List<String> optionsList3 = new ArrayList<>();

    private String[] furnitureArray4 = {"方才", "原材", "板材"};
    private List<String> optionsList4 = new ArrayList<>();
    private String[] furnitureArray5 = {"家具", "原材"};
    private List<String> optionsList5 = new ArrayList<>();
    private List<String> optionsList6 = new ArrayList<>();

    private String desc = "";
    private String category = "";
    private ArrayList<String> desc_image = new ArrayList();
    private boolean mIs_edit;
    private String mStore_id;
    private List<StoreManagerListEntity.GuigesEntity> mGuiges;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_commodity);
        ButterKnife.bind(this);
        initView();
        initJsonData();
        initArray();
    }


    private void initView() {
        mTvTitle.setText("商品规格");
        mBtnGo.setText("发布商品");

        StoreManagerListEntity store_entity = (StoreManagerListEntity) getIntent().getSerializableExtra("store_entity");
        mIs_edit = getIntent().getBooleanExtra("is_edit",false);


        mAdapter = new GridImageAdapterNew(this, onAddPicClickListener);
        //FullyGridLayoutManager manager = new FullyGridLayoutManager(PublishCommodityActivity.this, 3, GridLayoutManager.VERTICAL, false);
        //mRvPhoto.setLayoutManager(manager);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPhoto.setLayoutManager(manager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();

        snapHelper.attachToRecyclerView(mRvPhoto);


        mRvPhoto.setAdapter(mAdapter);
        mAdapter.setList(imageList);
        mAdapter.setSelectMax(maxSelectNum);
        mAdapter.setOnItemClickListener(new GridImageAdapterNew.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(PublishCommodityActivity.this).externalPicturePreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        if(store_entity != null){
            mStore_id = store_entity.id;
            mEtName.setText(store_entity.goods_name);
            mTvTypeName.setText(store_entity.category_type);
            category = store_entity.category;
            mEvTransportExpense.setText(store_entity.freight);
            mTvLocation.setText(store_entity.send_address);
            List<String> goods_image = store_entity.goods_image;
            String images = "";
            for (int i = 0; i < goods_image.size(); i++) {
                images = goods_image.get(i);
                imageList.add(images);
            }
            mAdapter.notifyDataSetChanged();

            List<String> desc_image = store_entity.desc_image;
            desc = store_entity.desc;
            this.desc_image.addAll(desc_image);
            mGood_spec = store_entity.skuList;
            mGuiges = store_entity.guiges;
/*            if(mGuiges!=null){
                if(mGuiges!=null&&mGuiges.size()>0){
                    for(int i=0;i<mGuiges.get(0).guigeArray.size()-1;i++){
                        StoreManagerListEntity.GuigesEntity entity = new StoreManagerListEntity.GuigesEntity();
                        entity.title = mGuiges.get(0).guigeArray.get(i);
                        mGuiges.add(entity);


                    }
                }
            }*/
            mTvDesc.setText("已填写");
            if(mGood_spec.size()==1){
                /*
                *              entity.sku_name = "默认规格";
                               entity.spec = "默认规格";
                * */
                if(mGood_spec.get(0).sku_name.equals("默认规格")){
                    mTvGoodsSpec.setText("未填写");
                    mEtNumber.setText(mGood_spec.get(0).stock);
                    mEtPrice.setText(mGood_spec.get(0).price);
                    mLlNumber.setVisibility(View.VISIBLE);
                    mLlPrice.setVisibility(View.VISIBLE);
                }else {
                    mTvGoodsSpec.setText("已填写");
                    mLlNumber.setVisibility(View.GONE);
                    mLlPrice.setVisibility(View.GONE);
                }
            }else {
                mTvGoodsSpec.setText("已填写");
                mLlNumber.setVisibility(View.GONE);
                mLlPrice.setVisibility(View.GONE);
            }
        }

        StringUtil.setSelectionEnd(mEtName);
        StringUtil.setSelectionEnd(mEvTransportExpense);

    }

    private GridImageAdapterNew.onAddPicClickListener onAddPicClickListener = new GridImageAdapterNew.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(PublishCommodityActivity.this)
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


    @OnClick({R.id.rl_back, R.id.ll_type_name, R.id.ll_goods_spec, R.id.ll_desc, R.id.ll_location, R.id.btn_go})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.ll_type_name:
                UIUtil.hideSoftInput(this,mLlTypeName);
                showSelect();
                break;
            case R.id.ll_goods_spec:
                intent = new Intent(this, GoodsSpecActivity.class);
                //if(mGood_spec.size()==1){
                /*
                *              entity.sku_name = "默认规格";
                               entity.spec = "默认规格";
                * */
                if(mGood_spec!=null&&mGood_spec.size()==1&&mGood_spec.get(0).sku_name.equals("默认规格")){
                    startActivityForResult(intent, SELECT_SPEC);
                    return;
                }
                if (mGood_spec != null) {
                    intent.putExtra("good_spec", (Serializable) mGood_spec);
                }
                if(mGuiges !=null){
                    intent.putExtra("good_guige", (Serializable) mGuiges);
                }
                startActivityForResult(intent, SELECT_SPEC);
                break;

            case R.id.ll_desc:
                intent = new Intent(this, GoodsDescActicity.class);
                intent.putExtra("desc",desc);
                intent.putStringArrayListExtra("image",desc_image);
                startActivityForResult(intent, SELECT_DESC);
                break;
            case R.id.ll_location:
                UIUtil.hideSoftInput(this,mLlLocation);
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
        if (mGood_spec == null||mGood_spec.size()==0) {
            String price = mEtPrice.getText().toString();
            if (TextUtils.isEmpty(price)) {
                UIUtil.toastShort(this, "请输入价格");
                return;
            }
            String number = mEtNumber.getText().toString();
            if (TextUtils.isEmpty(number)) {
                UIUtil.toastShort(this, "请输入库存");
                return;
            }
            mGood_spec = new ArrayList<>();
            StoreManagerListEntity.SkuListEntity entity = new StoreManagerListEntity.SkuListEntity();
            entity.price = price;
            entity.stock = number;
            entity.sku_name = "默认规格";
            entity.spec = "默认规格";
            mGood_spec.add(entity);
        }else{
            if(mGood_spec.size()==1&&"默认规格".equals(mGood_spec.get(0).spec)){
                String price = mEtPrice.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    UIUtil.toastShort(this, "请输入价格");
                    return;
                }
                String number = mEtNumber.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    UIUtil.toastShort(this, "请输入库存");
                    return;
                }
                StoreManagerListEntity.SkuListEntity entity = mGood_spec.get(0);
                entity.price = price;
                entity.stock = number;
                entity.sku_name = "默认规格";
                entity.spec = "默认规格";
            }

        }
        String name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            UIUtil.toastShort(this, "请输入商品名称");
            return;
        }
        String type_name = mTvTypeName.getText().toString();
        if (TextUtils.isEmpty(type_name)) {
            UIUtil.toastShort(this, "请选择商品类目");
            return;
        }
        String expense = mEvTransportExpense.getText().toString().trim();
        if (TextUtils.isEmpty(expense)) {
            UIUtil.toastShort(this, "请输入运费");
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            UIUtil.toastShort(this, "请添加物品描述");
            return;
        }
        String address = mTvLocation.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            UIUtil.toastShort(this, "请选择地址");
            return;
        }

        String goods = JSON.toJSONString(mGood_spec);//todo:
        String good_image="";
        for (int i = 0; i < imageList.size(); i++) {
            if (i < imageList.size() - 1) {
                good_image = good_image + imageList.get(i) + ";";
            } else {
                good_image = good_image + imageList.get(i);
            }
        }
        String good_desc_image="";
        for (int i = 0; i < desc_image.size(); i++) {
            if (i < desc_image.size() - 1) {
                good_desc_image = good_desc_image + desc_image.get(i) + ";";
            } else {
                good_desc_image = good_desc_image + desc_image.get(i);
            }
        }
        if (mIs_edit) {
            MineFuncManager.getInstance().sendGoodsUpdate(mStore_id,name,good_image,category,type_name,
                    expense,desc,good_desc_image,address,goods,
                    new ResponseCallback(){
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            EventBus.getDefault().post(new CreateGoodsEventy());
                            finish();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            UIUtil.showProgressBar(PublishCommodityActivity.this);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            UIUtil.hideProgressBar(PublishCommodityActivity.this);
                        }
                    });
        }else {
            MineFuncManager.getInstance().sendGoodsAdd(name, good_image, category, type_name,
                    expense, desc, good_desc_image, address, goods,
                    new ResponseCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            EventBus.getDefault().post(new CreateGoodsEventy());
                            finish();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            UIUtil.showProgressBar(PublishCommodityActivity.this);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            UIUtil.hideProgressBar(PublishCommodityActivity.this);
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

            case SELECT_SPEC:
                mGood_spec = (List<StoreManagerListEntity.SkuListEntity>) data.getSerializableExtra("good_spec");
                mGuiges = (List<StoreManagerListEntity.GuigesEntity>) data.getSerializableExtra("good_guige");
                if (mGood_spec==null || mGood_spec.size() == 0) {
                    mTvGoodsSpec.setText("未填写");
                    mLlNumber.setVisibility(View.VISIBLE);
                    mLlPrice.setVisibility(View.VISIBLE);
                }else {
                    mTvGoodsSpec.setText("已填写");
                    mLlPrice.setVisibility(View.GONE);
                    mLlNumber.setVisibility(View.GONE);
                }
                break;

            case SELECT_DESC:
                String desc = data.getStringExtra("desc");
                ArrayList<String> image_desc = data.getStringArrayListExtra("image");
                if (desc != null) {
                    this.desc = desc;
                    mTvDesc.setText("已填写");
                }else {
                    mTvDesc.setHint("未填写");
                }
                if (image_desc != null) {
                    desc_image.clear();
                    desc_image.addAll(image_desc);
                }
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
                            UIUtil.hideProgressBar(PublishCommodityActivity.this);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    //地址选择器
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
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(14)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void showJiaJu() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = optionsList1.get(options1) +
                        optionsList2.get(options2) +
                        optionsList3.get(options3);
                mTvTypeName.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(14)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setNPicker(optionsList1, optionsList2, optionsList3);//三级选择器
        pvOptions.show();
    }

    private void showYuanCai() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text =
                        optionsList2.get(options1) +
                                optionsList4.get(options2);
                mTvTypeName.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(14)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setNPicker(optionsList2,optionsList4,optionsList6);//三级选择器
        pvOptions.show();
    }

    private void showSelect() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = optionsList5.get(options1);
                category = text;
                if ("家具".equals(text)) {
                    showJiaJu();
                } else if ("原材".equals(text)) {
                    showYuanCai();
                }


            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(14)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(optionsList5);//三级选择器
        pvOptions.show();
    }


    private void initArray() {
        optionsList1.clear();
        optionsList2.clear();
        optionsList3.clear();
        optionsList4.clear();
        optionsList5.clear();
        for (int i = 0; i < furnitureArray1.length; i++) {
            optionsList1.add(furnitureArray1[i]);
        }
        for (int i = 0; i < furnitureArray2.length; i++) {
            optionsList2.add(furnitureArray2[i]);
        }
        for (int i = 0; i < furnitureArray3.length; i++) {
            optionsList3.add(furnitureArray3[i]);
        }
        for (int i = 0; i < furnitureArray4.length; i++) {
            optionsList4.add(furnitureArray4[i]);
        }
        for (int i = 0; i < furnitureArray5.length; i++) {
            optionsList5.add(furnitureArray5[i]);
        }
    }
}
