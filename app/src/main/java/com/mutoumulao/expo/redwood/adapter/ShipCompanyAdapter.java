package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.ShipCompanyEntity;

import java.util.List;

/**
 * Created by lzy on 2018/8/30.
 */

public class ShipCompanyAdapter extends BaseAdapter<ShipCompanyEntity,ShipCompanyAdapter.ShipCompanyViewHolder> {


    public ShipCompanyAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public ShipCompanyAdapter.ShipCompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShipCompanyViewHolder(mInflater.inflate(R.layout.item_ship_company, parent, false));

    }

    @Override
    public void onBindViewHolder(ShipCompanyAdapter.ShipCompanyViewHolder holder, int position) {
        ShipCompanyEntity item = getItem(position);
        holder.mTv_name.setText(item.ship_com_name);
    }

    class ShipCompanyViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTv_name;

        public ShipCompanyViewHolder(View itemView) {
            super(itemView);
            mTv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
