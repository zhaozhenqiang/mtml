package com.mutoumulao.expo.redwood.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseFragment;

/**
 * Created by lzy on 2018/8/14.
 */

public class StoresFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mRl_news;
    private TextView mTv_news;
    private RelativeLayout mRl_sales;
    private TextView mTv_sales;

    private FrameLayout fl_content;

    private String category = "";
    private int chooseIndex = -1;// 选中的索引
    private FragmentTransaction ft;

    private ImageView mIv_sales_sort;


    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_list, null);
    }

    @Override
    protected void initViews() {
        mRl_news = (RelativeLayout) findViewById(R.id.rl_news);
        mTv_news = (TextView) findViewById(R.id.tv_news);
        mRl_sales = (RelativeLayout) findViewById(R.id.rl_sales);
        mTv_sales = (TextView) findViewById(R.id.tv_sales);
        mIv_sales_sort = (ImageView) findViewById(R.id.iv_sales_sort);

        fl_content = (FrameLayout) findViewById(R.id.fl_content_store);

        mRl_news.setOnClickListener(this);
        mRl_sales.setOnClickListener(this);
        onClick(mRl_news);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
//        String position = mBundle.getString("arg");
        mIv_sales_sort.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        ft = getChildFragmentManager().beginTransaction();
        Bundle bundle;
        switch (v.getId()) {
            case R.id.rl_news:
                if (chooseIndex != 0) {
                    chooseIndex = 0;
                    tabBgChange(chooseIndex);
                    bundle = new Bundle();
                    bundle.putString("category", category);
                    bundle.putString("type", "is_new");
                    ft.replace(R.id.fl_content_store, StoresListFragment.instantiate(mBaseActivity, StoresListFragment.class.getName(), bundle), "StoresListFragment");

                }
                break;

            case R.id.rl_sales:
                if (chooseIndex != 1) {
                    chooseIndex = 1;
                    tabBgChange(chooseIndex);
                    bundle = new Bundle();
                    bundle.putString("category", category);
                    bundle.putString("type", "sale_num");
                    ft.replace(R.id.fl_content_store, StoresListFragment.instantiate(mBaseActivity, StoresListFragment.class.getName(), bundle), "StoresListFragment");
                }
                break;
            default:
                break;
        }
        ft.commit();
    }

    private void tabBgChange(int chooseIndex) {
        switch (chooseIndex) {
            case 0:
                mTv_news.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_red_price));
                mTv_sales.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));

                break;
            case 1:
                mTv_news.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));
                mTv_sales.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_red_price));

                break;
            case 2:
                mTv_news.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));
                mTv_sales.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));

                break;

            default:
                break;
        }
    }
}
