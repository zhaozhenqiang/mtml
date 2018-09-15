package com.mutoumulao.expo.redwood.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.OrderListActivity;
import com.mutoumulao.expo.redwood.activity.SettingActivity;
import com.mutoumulao.expo.redwood.adapter.MineFunAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.LoginEntity;
import com.mutoumulao.expo.redwood.entity.MineIconEntity;
import com.mutoumulao.expo.redwood.logic.LoginManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/1.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    public static String EXTECL_NAME = "name";
    public static String EXTECL_TYPE = "position";

    private TextView mTv_after_sale;
    private TextView mTv_evaluate;
    private TextView mTv_delivering;
    private TextView mTv_undeliver;
    private TextView mTv_unpay;
    private ImageView mIv_setting;
    private TextView mTv_name;
    private BaseRecyclerView mRv_fun;
    private CircleImageView mIv_head;

    private List<MineIconEntity> mList = new ArrayList<>();
    private String[] fun_name_business = {
            "招聘管理", "服务管理",
            "商铺管理", "我的足迹", "暂停营业",
            "联系客服", "关于我们"};

    private String[] fun_name_customer = {
            "商家入驻", "求职管理", "服务管理",
            "我的足迹",
            "联系客服", "关于我们"};

    private int[] fun_icon_business = {
            R.drawable.icon_recruitment_manager, R.drawable.icon_server_manager,
            R.drawable.icon_store_manager, R.drawable.icon_my_track, R.drawable.icon_stop_business,
            R.drawable.icon_call_service, R.drawable.icon_about_us};

    private int[] fun_icon_customer = {
            R.drawable.icon_store_entry, R.drawable.icon_recruitment_manager, R.drawable.icon_server_manager,
            R.drawable.icon_my_track,
            R.drawable.icon_call_service, R.drawable.icon_about_us};
    private MineFunAdapter mAdapter;
    private SharedPutils mSp;
    private TextView mTv_all_order;
    private BitmapTools mBitmapTools;


    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    protected void initViews() {


        mIv_head = (CircleImageView) findViewById(R.id.iv_head);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mIv_setting = (ImageView) findViewById(R.id.iv_setting);
        mTv_unpay = (TextView) findViewById(R.id.tv_unpay);
        mTv_undeliver = (TextView) findViewById(R.id.tv_undeliver);
        mTv_delivering = (TextView) findViewById(R.id.tv_delivering);
        mTv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        mTv_after_sale = (TextView) findViewById(R.id.tv_after_sale);
        mTv_all_order = (TextView) findViewById(R.id.tv_all_order);
        mRv_fun = (BaseRecyclerView) findViewById(R.id.rv_fun);

        mIv_setting.setOnClickListener(this);
        mTv_unpay.setOnClickListener(this);
        mTv_undeliver.setOnClickListener(this);
        mTv_delivering.setOnClickListener(this);
        mTv_evaluate.setOnClickListener(this);
        mTv_after_sale.setOnClickListener(this);
        mTv_all_order.setOnClickListener(this);

        mBitmapTools = new BitmapTools(mBaseActivity);
        mSp = SharedPutils.getPreferences(mBaseActivity);
        mTv_name.setText(TextUtils.isEmpty(mSp.getUserName()) ? "" : mSp.getUserName());
        mBitmapTools.display(mIv_head, TextUtils.isEmpty(mSp.getAvatar()) ? "" : UrlConst.IMAGE_URL + mSp.getAvatar());
        loadData();

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if ("1".equals(mSp.getIs_business())&& "1".equals(mSp.getState())) {
            for (int i = 0; i < fun_name_business.length; i++) {
                mList.add(new MineIconEntity(fun_name_business[i], getResources().getDrawable(fun_icon_business[i])));
                mAdapter = new MineFunAdapter(mBaseActivity, mList);
            }

        } else {
/*            for (int i = 0; i < fun_name_customer.length; i++) {
                mList.add(new MineIconEntity(fun_name_customer[i], getResources().getDrawable(fun_icon_customer[i])));
                mAdapter = new MineFunAdapter(mBaseActivity, mList);
            }*/
            for (int i = 0; i < fun_name_business.length; i++) {
                mList.add(new MineIconEntity(fun_name_business[i], getResources().getDrawable(fun_icon_business[i])));
                mAdapter = new MineFunAdapter(mBaseActivity, mList);
            }
        }
        mRv_fun.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_setting:
                intent = new Intent(mBaseActivity, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_all_order:
                intent = new Intent(mBaseActivity, OrderListActivity.class);
                intent.putExtra("position_order", 0);
                intent.putExtra("from", "custom");
                startActivity(intent);
                break;
            case R.id.tv_unpay:
                intent = new Intent(mBaseActivity, OrderListActivity.class);
                intent.putExtra("position_order", 1);
                intent.putExtra("from", "custom");
                startActivity(intent);
                break;
            case R.id.tv_undeliver:
                intent = new Intent(mBaseActivity, OrderListActivity.class);
                intent.putExtra("position_order", 2);
                intent.putExtra("from", "custom");
                startActivity(intent);
                break;
            case R.id.tv_delivering:
                intent = new Intent(mBaseActivity, OrderListActivity.class);
                intent.putExtra("position_order", 3);
                intent.putExtra("from", "custom");
                startActivity(intent);
                break;
            case R.id.tv_evaluate:
                intent = new Intent(mBaseActivity, OrderListActivity.class);
                intent.putExtra("position_order", 4);
                intent.putExtra("from", "custom");
                startActivity(intent);
                break;
            case R.id.tv_after_sale:
                intent = new Intent(mBaseActivity, OrderListActivity.class);
                intent.putExtra("position_order", 5);
                intent.putExtra("from", "custom");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //获取个人信息
    private void loadData() {
        LoginManager.getInstance().getUserInfo(new ResponseCallback<LoginEntity>() {
            @Override
            public void onSuccess(LoginEntity loginEntity) {
                super.onSuccess(loginEntity);
   /*             String avatar = loginEntity.avatar;
                mBitmapTools.display(mIv_head, UrlConst.IMAGE_URL + avatar);*/
            }
            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = mBaseActivity.getWindow();
        //如果系统5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bc_mine_fragment));
        }
    }
}
