package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.WuliuEntity;

import java.util.List;

/**
 * Created by lzy on 2018/8/30.
 */

public class WuliuAdapter extends BaseAdapter<WuliuEntity,WuliuAdapter.WuliuViewHolder> {

    public WuliuAdapter(Context context, List<WuliuEntity> list) {
        super(context, list);

    }

    @Override
    public WuliuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WuliuViewHolder(mInflater.inflate(R.layout.item_wuliu, parent, false));
    }

    @Override
    public void onBindViewHolder(WuliuViewHolder holder, int position) {
        WuliuEntity item = getItem(position);
        if(0 == position){
            holder.mTv_content.setTextColor(mContext.getResources().getColor(R.color.tc_red_price));
            holder.mTv_time.setTextColor(mContext.getResources().getColor(R.color.tc_red_price));
            holder.tv_time_out.setTextColor(mContext.getResources().getColor(R.color.tc_red_price));
            holder.view01.setBackground(mContext.getResources().getDrawable(R.drawable.shape_red_cycle_fill));
        }else {
            holder.mTv_content.setTextColor(mContext.getResources().getColor(R.color.tc_black3));
            holder.mTv_time.setTextColor(mContext.getResources().getColor(R.color.tc_black3));
            holder.tv_time_out.setTextColor(mContext.getResources().getColor(R.color.tc_black3));
            holder.view01.setBackground(mContext.getResources().getDrawable(R.drawable.shape_write_cycle_fill));

        }
        holder.mTv_content.setText(item.context);
        holder.mTv_time.setText(item.ftime);
        holder.tv_time_out.setText(item.time);
    }

    class WuliuViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTv_content;
        private final TextView mTv_time;
        private final TextView tv_time_out;
        private final View view01;

        public WuliuViewHolder(View itemView) {
            super(itemView);
            tv_time_out = itemView.findViewById(R.id.tv_time_out);
            mTv_content = itemView.findViewById(R.id.tv_content);
            mTv_time = itemView.findViewById(R.id.tv_time);
            view01 = itemView.findViewById(R.id.view01);
        }
    }
}
