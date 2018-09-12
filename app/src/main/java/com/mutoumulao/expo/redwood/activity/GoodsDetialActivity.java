package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GoodsDetialIndicatorAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;
import com.mutoumulao.expo.redwood.fragment.GoodsDetialFragment;
import com.mutoumulao.expo.redwood.fragment.GoodsImageDetialFragment;
import com.mutoumulao.expo.redwood.fragment.StoreCommentFragment;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
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
 * Created by lzy on 2018/8/8.
 */

public class GoodsDetialActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.pageindicator)
    TabPageIndicator mPageindicator;
    @BindView(R.id.vp_goods)
    ViewPager mVpGoods;
    @BindView(R.id.ll_empty)
    LinearLayout mLlEmpty;

    private String[] titles = {"商品", "详情", "评价"};
    private GoodsDetialIndicatorAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detial);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        String id = getIntent().getStringExtra("id");

        final List<Fragment> list = new ArrayList<>();


        GoodsDetialFragment goodsDetialFragment = new GoodsDetialFragment();
        GoodsImageDetialFragment goodsDetialFragment2 = new GoodsImageDetialFragment();
//        GoodsDetialFragment goodsDetialFragment3 = new GoodsDetialFragment();
        StoreCommentFragment goodsDetialFragment3 = new StoreCommentFragment();

        list.add(goodsDetialFragment);
        list.add(goodsDetialFragment2);
        list.add(goodsDetialFragment3);


        if (!TextUtils.isEmpty(id)) {
            MineFuncManager.getInstance().getGoodDetial(id, new ResponseCallback<GoodsDetialEntity>() {
                @Override
                public void onSuccess(GoodsDetialEntity storeDetialEntity) {
                    super.onSuccess(storeDetialEntity);
                    mVpGoods.setOffscreenPageLimit(2);
                    mAdapter = new GoodsDetialIndicatorAdapter(getSupportFragmentManager(), list, titles, storeDetialEntity);
                    mVpGoods.setAdapter(mAdapter);
                    mPageindicator.setViewPager(mVpGoods);
                    setTabPagerIndicator();
                }

                @Override
                public void onSuccess(List<GoodsDetialEntity> list) {
                    super.onSuccess(list);
                    if(list.size() == 0){
                        mVpGoods.setVisibility(View.GONE);
                        mLlEmpty.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public boolean isShowNotice() {
                    return false;
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(GoodsDetialActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(GoodsDetialActivity.this);
                }

                @Override
                public void onFailure(Exception e) {
                    super.onFailure(e);
                    UIUtil.toastShort(GoodsDetialActivity.this, e.toString());
                }



            });
        }


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

    private void setTabPagerIndicator() {
        mPageindicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式
        mPageindicator.setDividerColor(getResources().getColor(R.color.bg_line));// 设置分割线的颜色
        mPageindicator.setDividerPadding(DisplayUtil.dip2px(this, 10));
        mPageindicator.setIndicatorColor(getResources().getColor(R.color.tc_red));// 设置底部导航线的颜色
        mPageindicator.setTextColorSelected(getResources().getColor(R.color.tc_red));// 设置tab标题选中的颜色
        mPageindicator.setTextColor(getResources().getColor(R.color.tc_black6));// 设置tab标题未被选中的颜色
        mPageindicator.setTextSize(DisplayUtil.sp2px(this, 14));// 设置字体大小
    }
}
