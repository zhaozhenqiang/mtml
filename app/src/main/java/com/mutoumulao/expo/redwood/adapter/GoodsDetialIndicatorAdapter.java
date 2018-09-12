package com.mutoumulao.expo.redwood.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class GoodsDetialIndicatorAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsList;
    private String[] titles;
    private GoodsDetialEntity mStoreDetialEntity;


    public GoodsDetialIndicatorAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles, GoodsDetialEntity storeDetialEntity) {
        super(fm);
        this.fragmentsList = fragments;
        this.titles = titles;

        mStoreDetialEntity = storeDetialEntity;
    }


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("storeManagerListEntity", mStoreDetialEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
