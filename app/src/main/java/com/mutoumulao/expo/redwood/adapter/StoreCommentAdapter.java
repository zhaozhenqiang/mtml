package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.StoreCommentEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.view.CircleImageView;
import com.mutoumulao.expo.redwood.view.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/29.
 */

public class StoreCommentAdapter extends BaseAdapter<StoreCommentEntity, StoreCommentAdapter.StoreCommentViewHolder> {


    private final BitmapTools mBitmapTools;
    private BaseFragment mFragment;

    public StoreCommentAdapter(Context context, List<StoreCommentEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
    }

    @Override
    public StoreCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreCommentViewHolder(mInflater.inflate(R.layout.item_store_comment, parent, false));
    }

    public void setFragment(BaseFragment fragment){

        mFragment = fragment;
    }

    @Override
    public void onBindViewHolder(StoreCommentViewHolder holder, int position) {
        StoreCommentEntity item = getItem(position);

        mBitmapTools.display(holder.mIv_head, UrlConst.IMAGE_URL + item.avatar);
        holder.mTv_name.setText(item.nickname);
        holder.mTv_time.setText(item.time);
        holder.mTv_content.setText(item.content);
        if (!TextUtils.isEmpty(item.images)) {
            final List<String> list = JSON.parseArray(item.images, String.class);
            FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
            holder.mRv_photo.setLayoutManager(manager);
            OnlyDisplayGridImageAdapter adapter = new OnlyDisplayGridImageAdapter(mContext, list);

            holder.mRv_photo.setAdapter(adapter);

            adapter.setOnItemClickListener(new OnlyDisplayGridImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position1, View v) {
                    List<LocalMedia> medias = new ArrayList<>();
                    for (int i = 0; i <list.size(); i++) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(UrlConst.IMAGE_URL + list.get(i));
                        medias.add(localMedia);
//                        PictureSelector.create(mBaseActivity).externalPicturePreview(position, medias);
                    }
                        PictureSelector.create(mFragment.getActivity()).themeStyle( R.style.picture_default_style).openExternalPreview(position1, medias);
                }
            });

        }
    }

    class StoreCommentViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView mRv_photo;
        private final TextView mTv_content;
        private final TextView mTv_time;
        private final TextView mTv_name;
        private final CircleImageView mIv_head;

        public StoreCommentViewHolder(View itemView) {
            super(itemView);
            mIv_head = itemView.findViewById(R.id.iv_head);
            mTv_name = itemView.findViewById(R.id.tv_name);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_content = itemView.findViewById(R.id.tv_content);
            mRv_photo = itemView.findViewById(R.id.rv_photo);

        }
    }
}
