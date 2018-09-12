package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.BaseIndicatorAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.event.HomeServiceSearchEvent;
import com.mutoumulao.expo.redwood.fragment.HomeServerFragment;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.view.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/30.
 */

public class HomeServerActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rl_title)
    LinearLayout mRlTitle;
    @BindView(R.id.pageindicator)
    TabPageIndicator mPageindicator;
    @BindView(R.id.pager_news)
    ViewPager mPagerNews;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    private String[] titles = {"全部", "运输", "修理", "打包", "搬运", "配件", "物流", "油漆", "机器"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_service);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {


        List<Fragment> list = new ArrayList<>();
        HomeServerFragment fragment1 = new HomeServerFragment();
        HomeServerFragment fragment2 = new HomeServerFragment();
        HomeServerFragment fragment3 = new HomeServerFragment();
        HomeServerFragment fragment4 = new HomeServerFragment();
        HomeServerFragment fragment5 = new HomeServerFragment();
        HomeServerFragment fragment6 = new HomeServerFragment();
        HomeServerFragment fragment7 = new HomeServerFragment();
        HomeServerFragment fragment8 = new HomeServerFragment();
        HomeServerFragment fragment9 = new HomeServerFragment();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);
        list.add(fragment5);
        list.add(fragment6);
        list.add(fragment7);
        list.add(fragment8);
        list.add(fragment9);
        mPagerNews.setOffscreenPageLimit(1);
        BaseIndicatorAdapter adapter = new BaseIndicatorAdapter(getSupportFragmentManager(), list, titles);
        mPagerNews.setAdapter(adapter);

        mPageindicator.setViewPager(mPagerNews);
        setTabPagerIndicator();

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEtSearch.getText().toString().trim();
                        EventBus.getDefault().post(new HomeServiceSearchEvent(keyword));
                    /*if (!TextUtils.isEmpty(keyword)) {

                    } else {
                        UIUtil.toastShort(HomeServerActivity.this, "请输入关键字");
                    }*/
                }
                return false;
            }
        });
    }

    @OnClick({R.id.rl_back,R.id.iv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.iv_search:
                String keyword = mEtSearch.getText().toString().trim();
                    EventBus.getDefault().post(new HomeServiceSearchEvent(keyword));
                /*if (!TextUtils.isEmpty(keyword)) {

                } else {
                    UIUtil.toastShort(HomeServerActivity.this, "请输入关键字");
                }*/
                break;
        }
    }

    private void setTabPagerIndicator() {
        mPageindicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_NOWEIGHT_EXPAND_SAME);// 设置模式，一定要先设置模式
        mPageindicator.setDividerColor(getResources().getColor(R.color.bg_line));// 设置分割线的颜色
        mPageindicator.setDividerPadding(DisplayUtil.dip2px(this, 10));
        mPageindicator.setIndicatorColor(getResources().getColor(R.color.tc_red));// 设置底部导航线的颜色
        mPageindicator.setTextColorSelected(getResources().getColor(R.color.tc_red));// 设置tab标题选中的颜色
        mPageindicator.setTextColor(getResources().getColor(R.color.tc_black6));// 设置tab标题未被选中的颜色
        mPageindicator.setTextSize(DisplayUtil.sp2px(this, 14));// 设置字体大小
    }

}
