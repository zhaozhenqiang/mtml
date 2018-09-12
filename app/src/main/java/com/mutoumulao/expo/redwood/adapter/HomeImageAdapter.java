package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.util.BitmapTools;

import java.util.List;

/**
 * Created by lzy on 2018/8/10.
 */

public class HomeImageAdapter extends BaseAdapter<String, HomeImageAdapter.HomeImageViewHolder> {

    private final BitmapTools mBitmapTools;
    private int mLayoutType;

    public HomeImageAdapter(Context context, List<String> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
    }

    public void setLayoutType(int layoutType){
        mLayoutType = layoutType;
    }


    @Override
    public HomeImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewType = mLayoutType;
        switch (viewType) {
            case 0:
                return new HomeImageViewHolder(mInflater.inflate(R.layout.item_home_image, parent, false));
            case 1:
                return new HomeImageViewHolder(mInflater.inflate(R.layout.item_home_image_large, parent, false));
            default:
                return new HomeImageViewHolder(mInflater.inflate(R.layout.item_home_image, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(HomeImageViewHolder holder, int position) {
        String image = getItem(position);
        mBitmapTools.display(holder.mIv_home_image, UrlConst.IMAGE_URL + image);

    }

    class HomeImageViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_home_image;

        public HomeImageViewHolder(View itemView) {
            super(itemView);
            mIv_home_image = itemView.findViewById(R.id.iv_home_image);
        }
    }
}
