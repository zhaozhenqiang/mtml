package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;

import java.util.List;

/**
 * Created by lzy on 2018/8/23.
 */

public class ConfirmOrderdGoodsAdapter extends BaseAdapter<ShoppingEntity.ShoppingCarEntity, ConfirmOrderdGoodsAdapter.ConfirmOrderdGoodsViewHolder> {
    private BitmapTools mBitmapTools;

    public ConfirmOrderdGoodsAdapter(Context context, List<ShoppingEntity.ShoppingCarEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
    }

    @Override
    public ConfirmOrderdGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfirmOrderdGoodsViewHolder(mInflater.inflate(R.layout.item_confirm_orderd_goods, parent, false));

    }

    @Override
    public void onBindViewHolder(ConfirmOrderdGoodsViewHolder holder, int position) {
        ShoppingEntity.ShoppingCarEntity item = getItem(position);
        holder.mTv_goods_name.setText("商品: " +item.goods_name);
        holder.mTv_good_spec.setText("规格: " +item.spec);
        holder.mTv_goods_price.setText("价格: " +item.price);
        holder.mTv_goods_num.setText(TextUtils.isEmpty(item.num) ? "数量: 1" : "数量: " + item.num);
        if (item.goods_image != null && item.goods_image.size() > 0) {
            mBitmapTools.display(holder.mIv_goods_head, UrlConst.IMAGE_URL + item.goods_image.get(0));
        }
    }

    class ConfirmOrderdGoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_goods_head;
        private final TextView mTv_goods_name;
        private final TextView mTv_good_spec;
        private final TextView mTv_goods_price;
        private final TextView mTv_goods_num;

        public ConfirmOrderdGoodsViewHolder(View itemView) {
            super(itemView);
            mIv_goods_head = itemView.findViewById(R.id.iv_goods_head);
            mTv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            mTv_good_spec = itemView.findViewById(R.id.tv_good_spec);
            mTv_goods_price = itemView.findViewById(R.id.tv_goods_price);
            mTv_goods_num = itemView.findViewById(R.id.tv_goods_num);
        }
    }
}
