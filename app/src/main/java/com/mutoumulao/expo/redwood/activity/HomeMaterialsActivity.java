package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.mutoumulao.expo.redwood.entity.event.HomeSearchEvent;
import com.mutoumulao.expo.redwood.fragment.MaterialsFragment;
import com.mutoumulao.expo.redwood.fragment.StoresFragment;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.view.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/13.
 * 首页原材料
 */

public class HomeMaterialsActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rl_title)
    LinearLayout mRlTitle;
    @BindView(R.id.pageindicator)
    TabPageIndicator mPageindicator;
    @BindView(R.id.pager_materials)
    ViewPager mPagerMaterials;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;

    private String[] titles = {"家具", "原材", "厂家"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_materials);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        final String keyword = getIntent().getStringExtra("keyword");
        int position = getIntent().getIntExtra("position",1);
        if (!TextUtils.isEmpty(keyword)) {
            mEtSearch.setText(keyword);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    EventBus.getDefault().post(new HomeSearchEvent(keyword));
                }
            }, 50);

        }
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEtSearch.getText().toString().trim();
                        EventBus.getDefault().post(new HomeSearchEvent(keyword));
                    /*if (!TextUtils.isEmpty(keyword)) {

                    } else {
                        UIUtil.toastShort(HomeMaterialsActivity.this, "请输入关键字");
                    }*/
                }
                return false;
            }
        });
        List<Fragment> list = new ArrayList<>();

        MaterialsFragment frament1 = new MaterialsFragment();
        MaterialsFragment frament2 = new MaterialsFragment();
        StoresFragment frament3 = new StoresFragment();

        list.add(frament1);
        list.add(frament2);
        list.add(frament3);

        mPagerMaterials.setOffscreenPageLimit(1);
        BaseIndicatorAdapter adapter = new BaseIndicatorAdapter(getSupportFragmentManager(), list, titles);
        mPagerMaterials.setAdapter(adapter);
        mPagerMaterials.setCurrentItem(position);
        mPageindicator.setCurrentPosition(position);
        mPageindicator.setViewPager(mPagerMaterials);

        setTabPagerIndicator();
    }

    @OnClick({R.id.rl_back,R.id.iv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_search:
                String keyword = mEtSearch.getText().toString().trim();
                    EventBus.getDefault().post(new HomeSearchEvent(keyword));
                /*if (!TextUtils.isEmpty(keyword)) {

                } else {
                    UIUtil.toastShort(HomeMaterialsActivity.this, "请输入关键字");
                }*/
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
