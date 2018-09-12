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
import com.mutoumulao.expo.redwood.entity.OrderEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;

import java.util.List;

/**
 * Created by lzy on 2018/8/11.
 */

public class OrderGoodsAdapter extends BaseAdapter<OrderEntity.ListEntity, OrderGoodsAdapter.ShoppingGoodsViewHolder> {

    private BitmapTools mBitmapTools;


    public OrderGoodsAdapter(Context context, List<OrderEntity.ListEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);


    }


    @Override
    public ShoppingGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShoppingGoodsViewHolder(mInflater.inflate(R.layout.item_order_goods, parent, false));

    }

    @Override
    public void onBindViewHolder(final ShoppingGoodsViewHolder holder, final int position) {
        final OrderEntity.ListEntity item = getItem(position);
        holder.mTv_goods_name.setText(item.goods_name);
        holder.mTv_goods_price.setText(item.sku_info.price);
        holder.mTv_num.setText("×" + item.num);
        holder.mTv_spec.setText(item.sku_info.spec);
        if (item.goods_image != null && item.goods_image.size() > 0) {
            mBitmapTools.display(holder.mIv_goods_head, UrlConst.IMAGE_URL + item.goods_image.get(0));
        }




    }


    class ShoppingGoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_goods_head;
        private final TextView mTv_goods_name;
        private final TextView mTv_goods_price;
        private final TextView mTv_spec;
        private final TextView mTv_num;


        public ShoppingGoodsViewHolder(View itemView) {
            super(itemView);

            mIv_goods_head = itemView.findViewById(R.id.iv_goods_head);
            mTv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            mTv_goods_price = itemView.findViewById(R.id.tv_goods_price);
            mTv_spec = itemView.findViewById(R.id.tv_spec);
            mTv_num = itemView.findViewById(R.id.tv_num);

        }
    }

}
