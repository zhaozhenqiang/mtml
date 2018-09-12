package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.CreateAddressActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.AddressEntity;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;

import java.util.List;

/**
 * Created by lzy on 2018/8/19.
 */

public class AddressListAdapter extends BaseAdapter<AddressEntity,AddressListAdapter.AddressLstViewHolder> {


    public AddressListAdapter(Context context, List<AddressEntity> list) {
        super(context, list);
    }

    @Override
    public AddressLstViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressLstViewHolder(mInflater.inflate(R.layout.item_address_list, null));
    }

    @Override
    public void onBindViewHolder(AddressLstViewHolder holder, final int position) {
        final AddressEntity item = getItem(position);
        holder.mTv_name.setText("收货人:" + item.name);
         holder.mTv_number.setText(item.phone);
         holder.mTv_content.setText("收货地址:" + item.address);
         holder.mTv_edit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CreateAddressActivity.class).putExtra("address",item));
             }
         });
         holder.mTv_delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ShoppingManager.getInstance().delAddress(item.id,new ResponseCallback(){
                     @Override
                     public void onSuccess(Object o) {
                         super.onSuccess(o);
                         mItems.remove(position);
                         notifyDataSetChanged();
                     }
                 });
             }
         });
    }

    class AddressLstViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTv_name;
        private final TextView mTv_number;
        private final TextView mTv_content;
        private final TextView mTv_edit;
        private final TextView mTv_delete;

        public AddressLstViewHolder(View itemView) {
            super(itemView);
            mTv_name = itemView.findViewById(R.id.tv_name);
            mTv_number = itemView.findViewById(R.id.tv_number);
            mTv_content = itemView.findViewById(R.id.tv_content);
            mTv_edit = itemView.findViewById(R.id.tv_edit);
            mTv_delete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
