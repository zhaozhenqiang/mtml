package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;

import java.util.List;

/**
 * Created by lzy on 2018/8/8.
 * 商家信息
 */

public class StoreListAdapter extends BaseAdapter<StoreEntity, StoreListAdapter.StoreListViewHolder> {

    private final BitmapTools mBitmapTools;

    public StoreListAdapter(Context context, List<StoreEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
    }

    @Override
    public StoreListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreListViewHolder(mInflater.inflate(R.layout.item_store_list, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreListViewHolder holder, int position) {
        final StoreEntity entity = mItems.get(position);
        mBitmapTools.display(holder.mIv_store_head, UrlConst.IMAGE_URL + entity.avatar);
        holder.mTv_store_name.setText(entity.business_name);
        holder.mTv_content.setText(entity.description);
        holder.mTv_time.setText(entity.updated_at);

    }

    class StoreListViewHolder extends RecyclerView.ViewHolder {


        private final ImageView mIv_store_head;
        private final TextView mTv_store_name;
        private final TextView mTv_content;
        private final TextView mTv_time;

        public StoreListViewHolder(View itemView) {
            super(itemView);
            mIv_store_head = itemView.findViewById(R.id.iv_store_head);
            mTv_store_name = itemView.findViewById(R.id.tv_store_name);
            mTv_content = itemView.findViewById(R.id.tv_content);
            mTv_time = itemView.findViewById(R.id.tv_time);

        }
    }
}
