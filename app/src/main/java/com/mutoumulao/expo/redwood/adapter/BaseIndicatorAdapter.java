package com.mutoumulao.expo.redwood.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class BaseIndicatorAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsList;
    private String[] titles;


    public BaseIndicatorAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragmentsList = fragments;
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("arg", position + "");
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
