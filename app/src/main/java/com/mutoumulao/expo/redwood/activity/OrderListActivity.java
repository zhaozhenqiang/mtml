package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.OrderIndicatorAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.fragment.OrderListFragment;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.view.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/13.
 * 订单列表
 */

public class OrderListActivity extends BaseActivity {

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
    @BindView(R.id.pageindicator)
    TabPageIndicator mPageindicator;
    @BindView(R.id.pager_news)
    ViewPager mPagerNews;
    private String[] titles = {"全部", "待付款", "待发货", "已发货", "已签收", "已完成", "已取消"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTitle.setText("订单");
        int position = getIntent().getIntExtra("position_order", 0);
        String from = getIntent().getStringExtra("from");
        String is_business = "";
        if ("custom".equals(from)) {
            //普通用户
            is_business = "0";
        } else {
            is_business = "1";
        }
        List<Fragment> list = new ArrayList<>();
        OrderListFragment fragment1 = new OrderListFragment();
        OrderListFragment fragment2 = new OrderListFragment();
        OrderListFragment fragment3 = new OrderListFragment();
        OrderListFragment fragment4 = new OrderListFragment();
        OrderListFragment fragment5 = new OrderListFragment();
        OrderListFragment fragment6 = new OrderListFragment();
        OrderListFragment fragment7 = new OrderListFragment();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);
        list.add(fragment5);
        list.add(fragment6);
        list.add(fragment7);
        mPagerNews.setOffscreenPageLimit(1);
        OrderIndicatorAdapter adapter = new OrderIndicatorAdapter(getSupportFragmentManager(), list, titles,is_business);
        mPagerNews.setAdapter(adapter);
        mPagerNews.setCurrentItem(position);
        mPageindicator.setCurrentPosition(position);
        mPageindicator.setViewPager(mPagerNews);
        setTabPagerIndicator();
    }


    private void setTabPagerIndicator() {
        mPageindicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_NOWEIGHT_EXPAND_NOSAME);// 设置模式，一定要先设置模式
        mPageindicator.setDividerColor(getResources().getColor(R.color.bg_line));// 设置分割线的颜色
        mPageindicator.setDividerPadding(DisplayUtil.dip2px(this, 10));
        mPageindicator.setIndicatorColor(getResources().getColor(R.color.tc_red));// 设置底部导航线的颜色
        mPageindicator.setTextColorSelected(getResources().getColor(R.color.tc_red));// 设置tab标题选中的颜色
        mPageindicator.setTextColor(getResources().getColor(R.color.tc_black6));// 设置tab标题未被选中的颜色
        mPageindicator.setTextSize(DisplayUtil.sp2px(this, 14));// 设置字体大小
    }

    @OnClick(R.id.rl_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            default:
                break;
        }
    }
}
