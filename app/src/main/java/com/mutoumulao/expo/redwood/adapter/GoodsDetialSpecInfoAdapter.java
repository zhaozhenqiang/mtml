package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.SelectEntity;

import java.util.List;

/**
 * Created by lzy on 2018/8/18.
 */

public class GoodsDetialSpecInfoAdapter extends BaseAdapter<SelectEntity, GoodsDetialSpecInfoAdapter.GoodsDetialSpecInfoViewHolder> {


    public GoodsDetialSpecInfoAdapter(Context context, List<SelectEntity> list) {
        super(context, list);
    }

    @Override
    public GoodsDetialSpecInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //selector_goods_spec
        return new GoodsDetialSpecInfoViewHolder(mInflater.inflate(R.layout.item_goods_spec_infoselect, null));

    }

    @Override
    public void onBindViewHolder(final GoodsDetialSpecInfoViewHolder holder, int position) {
        final SelectEntity item = getItem(position);

        holder.mTv_type_name.setText(item.name);
        holder.mTv_type_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isCheck) {
                    item.isCheck = false;
                    holder.mTv_type_name.setTextColor(mContext.getResources().getColor(R.color.tc_write));
                } else {
                    item.isCheck = true;
                    holder.mTv_type_name.setTextColor(mContext.getResources().getColor(R.color.tc_black6));
                }
            }
        });

    }

    class GoodsDetialSpecInfoViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_type_name;

        public GoodsDetialSpecInfoViewHolder(View itemView) {
            super(itemView);
            mTv_type_name = itemView.findViewById(R.id.tv_type_name);
        }
    }
}
