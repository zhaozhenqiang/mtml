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

public class MaterialsFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mRl_news;
    private TextView mTv_news;
    private RelativeLayout mRl_sales;
    private TextView mTv_sales;
    private TextView mTv_price;
    private FrameLayout fl_content;

    private String category = "";
    private int chooseIndex = -1;// 选中的索引
    private FragmentTransaction ft;
    private RelativeLayout mRl_price;
    private ImageView mIv_sales_sort;
    private ImageView mIv_price_sort;
    private int sales_sort = 1;
    private int price_sort = 1;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_materials_list, null);
    }

    @Override
    protected void initViews() {
        mRl_news = (RelativeLayout) findViewById(R.id.rl_news);
        mTv_news = (TextView) findViewById(R.id.tv_news);
        mRl_sales = (RelativeLayout) findViewById(R.id.rl_sales);
        mTv_sales = (TextView) findViewById(R.id.tv_sales);
        mIv_sales_sort = (ImageView) findViewById(R.id.iv_sales_sort);
        mRl_price = (RelativeLayout) findViewById(R.id.rl_price);
        mTv_price = (TextView) findViewById(R.id.tv_price);
        mIv_price_sort = (ImageView) findViewById(R.id.iv_price_sort);
        fl_content = (FrameLayout) findViewById(R.id.fl_content);

        mRl_news.setOnClickListener(this);
        mRl_sales.setOnClickListener(this);
        mRl_price.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        String position = mBundle.getString("arg");

        if ("0".equals(position)) {
            category = "家具";
        } else if ("1".equals(position)) {
            category = "原材";
        } else if ("2".equals(position)) {
            category = "厂家";
            mRl_sales.setVisibility(View.GONE);
        } else {
            category = "家具";
        }

        onClick(mRl_news);

    }

    @Override
    public void onClick(View v) {
        ft = getChildFragmentManager().beginTransaction();
        Bundle bundle;
        switch (v.getId()) {
            case R.id.rl_news:
                if (chooseIndex != 0) {
                    chooseIndex = 0;
                    sales_sort = 1;
                    price_sort = 1;
                    mIv_price_sort.setBackgroundResource(R.drawable.icon_all_gray);
                    mIv_sales_sort.setBackgroundResource(R.drawable.icon_all_gray);
                    tabBgChange(chooseIndex);
                    bundle = new Bundle();
                    bundle.putString("category", category);
                    bundle.putString("type", "is_new");

                    ft.replace(R.id.fl_content, MeterialsListFragment.instantiate(mBaseActivity, MeterialsListFragment.class.getName(), bundle), "MeterialsListFragment");

                }
                break;
            case R.id.rl_sales:
                chooseIndex = 1;
                tabBgChange(chooseIndex);
                bundle = new Bundle();
                mIv_price_sort.setBackgroundResource(R.drawable.icon_all_gray);

                if (sales_sort == 2) {
                    sales_sort = 1;
                    mIv_sales_sort.setBackgroundResource(R.drawable.icon_top_red);
                } else if (sales_sort == 1) {
                    sales_sort = 2;
                    mIv_sales_sort.setBackgroundResource(R.drawable.icon_bottom_red);
                }
                bundle.putString("category", category);
                bundle.putString("type", "sale_num");
                bundle.putInt("sort", sales_sort);
                ft.replace(R.id.fl_content, MeterialsListFragment.instantiate(mBaseActivity, MeterialsListFragment.class.getName(), bundle), "MeterialsListFragment");

                break;
            case R.id.rl_price:
                mIv_sales_sort.setBackgroundResource(R.drawable.icon_all_gray);
                chooseIndex = 2;
                tabBgChange(chooseIndex);
                bundle = new Bundle();
                if (price_sort == 2) {
                    price_sort = 1;
                    mIv_price_sort.setBackgroundResource(R.drawable.icon_top_red);
                } else if (price_sort == 1) {
                    price_sort = 2;
                    mIv_price_sort.setBackgroundResource(R.drawable.icon_bottom_red);
                }
                bundle.putString("category", category);
                bundle.putString("type", "price");
                bundle.putInt("sort", price_sort);
                ft.replace(R.id.fl_content, MeterialsListFragment.instantiate(mBaseActivity, MeterialsListFragment.class.getName(), bundle), "MeterialsListFragment");

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
                mTv_price.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));

                break;
            case 1:
                mTv_news.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));
                mTv_sales.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_red_price));
                mTv_price.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));

                break;
            case 2:
                mTv_news.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));
                mTv_sales.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black3));
                mTv_price.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_red_price));

                break;

            default:
                break;
        }
    }
}
