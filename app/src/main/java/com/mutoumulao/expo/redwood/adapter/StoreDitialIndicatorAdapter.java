package com.mutoumulao.expo.redwood.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class StoreDitialIndicatorAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsList;
    private String[] titles;
    private final String mId;
    private final String mUid;


    public StoreDitialIndicatorAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles,String id,String uid) {
        super(fm);
        this.fragmentsList = fragments;
        this.titles = titles;
        mId = id;
        mUid = uid;
    }


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("store_id", mId);
        bundle.putString("uid", mUid);
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
