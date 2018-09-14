package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;

import java.util.List;

/**
 * Created by lzy on 2018/8/3.
 */

public class HomeFuncListAdapter extends BaseAdapter<PositionEntity, HomeFuncListAdapter.FuncListViewHolder> {

    private ImageRecyclerReduceItemListener mOnDeleteListerer;
    private ImageRecyclerReduceItemListener mOnEditListener;

    public HomeFuncListAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public FuncListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuncListViewHolder(mInflater.inflate(R.layout.item_func_list, null));
    }

    @Override
    public void onBindViewHolder(FuncListViewHolder holder, final int position) {
        PositionEntity item = getItem(position);
        holder.mTv_content.setText(item.content);
        holder.mTv_title.setText(item.title);
        holder.mTv_time.setText(item.time);
        holder.mTv_delete.setVisibility(View.GONE);
        holder.mTv_edit.setVisibility(View.GONE);
    }


    class FuncListViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_title;
        private final TextView mTv_content;
        private final TextView mTv_time;
        private final TextView mTv_edit;
        private final TextView mTv_delete;

        public FuncListViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_title);
            mTv_content = itemView.findViewById(R.id.tv_content);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_edit = itemView.findViewById(R.id.tv_edit);
            mTv_delete = itemView.findViewById(R.id.tv_delete);

        }
    }

    public void setOnDeleteListerer(ImageRecyclerReduceItemListener onDeleteListerer) {
        mOnDeleteListerer = onDeleteListerer;
    }

    public void setOnEditListener(ImageRecyclerReduceItemListener onEditListener) {
        mOnEditListener = onEditListener;
    }
}
