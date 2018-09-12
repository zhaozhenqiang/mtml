package com.mutoumulao.expo.redwood.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.HomeImageAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/27.
 */

public class GoodsImageDetialFragment extends BaseFragment {

    private TextView mTv_content;
    private BaseRecyclerView mRv_photo;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_image_detial, null);
    }

    @Override
    protected void initViews() {
        mRv_photo = (BaseRecyclerView) findViewById(R.id.rv_photo);
        mTv_content = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        GoodsDetialEntity entity = (GoodsDetialEntity) mBundle.getSerializable("storeManagerListEntity");
        final List<String> images = new ArrayList<>();//图片集合
        if (entity != null) {
            for (int i = 0; i < entity.desc_image.size(); i++) {
                images.add(/*UrlConst.IMAGE_URL + */entity.desc_image.get(i));
            }
        }
        if (images != null && images.size() > 0) {
            HomeImageAdapter imageAdapter = new HomeImageAdapter(mBaseActivity, images);
            mRv_photo.setAdapter(imageAdapter);
            imageAdapter.setLayoutType(1);

            mRv_photo.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, long id) {
                    List<LocalMedia> medias = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(UrlConst.IMAGE_URL + images.get(i));
                        medias.add(localMedia);
//                        PictureSelector.create(mBaseActivity).externalPicturePreview(position, medias);
                    }
                    PictureSelector.create(mBaseActivity).themeStyle(R.style.picture_default_style).openExternalPreview(position, medias);
                }
            });
        }

        mTv_content.setText(entity.desc);

    }
}
