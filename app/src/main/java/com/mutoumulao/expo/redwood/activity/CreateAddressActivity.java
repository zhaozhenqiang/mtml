package com.mutoumulao.expo.redwood.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.AddressEntity;
import com.mutoumulao.expo.redwood.entity.JsonEntity;
import com.mutoumulao.expo.redwood.entity.event.AdressEvent;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.JsonFileReader;
import com.mutoumulao.expo.redwood.util.StringUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/19.
 */

public class CreateAddressActivity extends BaseActivity {

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
    @BindView(R.id.et_number)
    EditText mEtNumber;
    @BindView(R.id.et_location)
    TextView mEtLocation;
    @BindView(R.id.rl_location)
    LinearLayout mLlLocation;
    @BindView(R.id.et_detial)
    EditText mEtDetial;
    @BindView(R.id.tb_default)
    ToggleButton mTbDefault;
    @BindView(R.id.btn_go)
    Button mBtnGo;

    private String province = "";
    private String city = "";
    private String area = "";
    private String isDefault = "0";//(是否默认：0不是默认，1默认地址)
    private boolean isEdit;

    private ArrayList<JsonEntity> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private AddressEntity mAddressEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);
        ButterKnife.bind(this);
        initView();
        initJsonData();
    }

    private void initView() {
        mTvTitle.setText("新增收货地址");
        mBtnGo.setText("保存");

        mTbDefault.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                hideSoftInput(CreateAddressActivity.this,mLlLocation);
                if (on) {
                    isDefault = "1";
                } else {
                    isDefault = "0";

                }
            }
        });

        mAddressEntity = (AddressEntity) getIntent().getSerializableExtra("address");
        if (mAddressEntity != null) {
            isEdit = true;
            isDefault = mAddressEntity.is_default;
            mEtName.setText(mAddressEntity.name);
            mEtNumber.setText(mAddressEntity.phone);
            mEtDetial.setText(mAddressEntity.address);
            province = mAddressEntity.province_name;
            city = mAddressEntity.city_name;
            area = mAddressEntity.area_name;
            mEtLocation.setText(mAddressEntity.province_name + mAddressEntity.city_name + mAddressEntity.area_name);
            if ("0".equals(mAddressEntity.is_default)) {
                mTbDefault.setToggleOn();
            } else {
                mTbDefault.setToggleOff();

            }
        }

        StringUtil.setSelectionEnd(mEtName);
        StringUtil.setSelectionEnd(mEtNumber);
        StringUtil.setSelectionEnd(mEtDetial);
    }

    @OnClick({R.id.rl_back, R.id.rl_location, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_location:
                hideSoftInput(this,mLlLocation);
                showPickerView();
                break;
            case R.id.btn_go:
                addAddress();

                break;
            default:
                break;
        }
    }

    private void addAddress() {
        AddressEntity entity = new AddressEntity();
        String name = mEtName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            entity.name = name;
        } else {
            UIUtil.toastShort(this, "请输入姓名");
            return;
        }
        String mobile = mEtNumber.getText().toString().trim();
        if (StringUtil.isMobileNumber(mobile)) {
            entity.phone = mobile;
        } else {
            UIUtil.toastShort(this, "请输入正确的手机号码");
            return;
        }
        if (!TextUtils.isEmpty(province)) {
            entity.province_name = province;
            entity.city_name = city;
            entity.area_name = area;
        } else {
            UIUtil.toastShort(this, "请选择省市区");
            return;
        }
        String address = mEtDetial.getText().toString();
        if (!TextUtils.isEmpty(address)) {
            entity.address = address;
        } else {
            UIUtil.toastShort(this, "请输入详细地址");
            return;
        }
        entity.is_default = isDefault;

        if (isEdit) {
            ShoppingManager.getInstance().updateAddress(entity,mAddressEntity.id, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    finish();
                    EventBus.getDefault().post(new AdressEvent());
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(CreateAddressActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(CreateAddressActivity.this);
                }
            });

        }else {
            ShoppingManager.getInstance().addAddress(entity, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    finish();
                    EventBus.getDefault().post(new AdressEvent());
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(CreateAddressActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(CreateAddressActivity.this);
                }
            });
        }
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
                mEtLocation.setText(text);

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

    private void hideSoftInput(Context context, View view) {
        //隐藏键盘时取消对etcommenter的监听;
        //  mEt_sendmessage.removeTextChangedListener(new MyTextWatcher(ivCommentSend));
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
