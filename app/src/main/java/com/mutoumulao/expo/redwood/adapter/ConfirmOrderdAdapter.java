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
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.util.List;

/**
 * Created by lzy on 2018/8/9.
 */

public class ConfirmOrderdAdapter extends BaseAdapter<ShoppingEntity, ConfirmOrderdAdapter.ShoppingViewHolder> {

    private BitmapTools mBitmapTools;

    public ConfirmOrderdAdapter(Context context, List<ShoppingEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);

    }

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShoppingViewHolder(mInflater.inflate(R.layout.item_shopping_car, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        ShoppingEntity item = getItem(position);
        mBitmapTools.display(holder.mIv_store_head, UrlConst.IMAGE_URL + item.avatar);
        holder.mTv_store_name.setText(item.business_name);

        ConfirmOrderdGoodsAdapter mAdapter = new ConfirmOrderdGoodsAdapter(mContext, item.list);
        holder.mRv_goods.setAdapter(mAdapter);

    }

    class ShoppingViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIv_store_head;
        private TextView mTv_store_name;
        private BaseRecyclerView mRv_goods;

        public ShoppingViewHolder(View itemView) {
            super(itemView);
            mIv_store_head = itemView.findViewById(R.id.iv_store_head);
            mTv_store_name = itemView.findViewById(R.id.tv_store_name);
            mRv_goods = itemView.findViewById(R.id.rv_goods);

        }
    }



}
