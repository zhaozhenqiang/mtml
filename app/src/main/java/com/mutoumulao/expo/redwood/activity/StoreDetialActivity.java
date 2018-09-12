package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.StoreDitialIndicatorAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
import com.mutoumulao.expo.redwood.fragment.StoreDeitalGoodsFragment;
import com.mutoumulao.expo.redwood.fragment.StoreDetialInviteFragment;
import com.mutoumulao.expo.redwood.fragment.StoreInfoDetialFragment;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/24.
 * 商家详情
 */

public class StoreDetialActivity extends BaseActivity {
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
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.iv_phone)
    ImageView mIvPhone;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.iv_call)
    TextView mTvCall;
    @BindView(R.id.pageindicator)
    TabPageIndicator mPageindicator;
    @BindView(R.id.pager_news)
    ViewPager mPagerNews;
    @BindView(R.id.iv_head)
    ImageView mIvHead;

    private String mPhone;

    private String[] titles = {"全部商品", "商家详情", "招聘信息"};
    private String mAuthor_id;
    private String mUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detial);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTitle.setText("商家详情");

        String form = getIntent().getStringExtra("form");
        String store_id = getIntent().getStringExtra("store_id");
        if (store_id != null) {
            mAuthor_id = getIntent().getStringExtra("id");
            mUid = getIntent().getStringExtra("author_id");
            loadStore(store_id);
        }


        StoreEntity storeEntity = (StoreEntity) getIntent().getSerializableExtra("storeEntity");
        if (storeEntity != null) {
            new BitmapTools(this).display(mIvHead, UrlConst.IMAGE_URL + storeEntity.avatar);
            mPhone = storeEntity.phone;
            mTvPhone.setText(mPhone);

            mTvLocation.setText(storeEntity.province_name + storeEntity.city_name + storeEntity.area_name + storeEntity.address);
            mTvName.setText(storeEntity.business_name);
            mAuthor_id = storeEntity.id;
            mUid = storeEntity.author_id;
        }

        List<Fragment> list = new ArrayList<>();
        StoreDeitalGoodsFragment fragment1 = new StoreDeitalGoodsFragment();
        StoreInfoDetialFragment fragment2 = new StoreInfoDetialFragment();
        StoreDetialInviteFragment fragment3 = new StoreDetialInviteFragment();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);

        mPagerNews.setOffscreenPageLimit(1);
        StoreDitialIndicatorAdapter adapter = new StoreDitialIndicatorAdapter(getSupportFragmentManager(), list, titles, mAuthor_id, mUid);
        mPagerNews.setAdapter(adapter);
        mPageindicator.setViewPager(mPagerNews);
        setTabPagerIndicator();

    }


    private void setTabPagerIndicator() {
        mPageindicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式
        mPageindicator.setDividerColor(getResources().getColor(R.color.bg_line));// 设置分割线的颜色
        mPageindicator.setDividerPadding(DisplayUtil.dip2px(this, 10));
        mPageindicator.setIndicatorColor(getResources().getColor(R.color.tc_red));// 设置底部导航线的颜色
        mPageindicator.setTextColorSelected(getResources().getColor(R.color.tc_red));// 设置tab标题选中的颜色
        mPageindicator.setTextColor(getResources().getColor(R.color.tc_black6));// 设置tab标题未被选中的颜色
        mPageindicator.setTextSize(DisplayUtil.sp2px(this, 14));// 设置字体大小
    }


    private void loadStore(String store_id) {
        MineFuncManager.getInstance().geStoreDetial(store_id, new ResponseCallback<StoreEntity>() {
            @Override
            public void onSuccess(StoreEntity storeEntity) {
                super.onSuccess(storeEntity);

                new BitmapTools(StoreDetialActivity.this).display(mIvHead, UrlConst.IMAGE_URL + storeEntity.avatar);
                mPhone = storeEntity.phone;
                mTvPhone.setText(mPhone);
                mTvLocation.setText(storeEntity.province_name + storeEntity.city_name + storeEntity.area_name + storeEntity.address);
                mTvName.setText(storeEntity.business_name);
                mAuthor_id = storeEntity.id;
                mUid = storeEntity.author_id;

            }

            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }

    @OnClick({R.id.rl_back, R.id.iv_call})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_call:
                if (TextUtils.isEmpty(mPhone)) {
                    UIUtil.toastShort(this, "未留电话");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mPhone);
                intent.setData(data);
                startActivity(intent);
                break;
            default:
        }
    }
}
