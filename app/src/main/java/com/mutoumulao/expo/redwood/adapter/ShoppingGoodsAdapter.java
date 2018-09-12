package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.CheckClickListener;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.view.AmountView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lzy on 2018/8/11.
 */

public class ShoppingGoodsAdapter extends BaseAdapter<ShoppingEntity.ShoppingCarEntity, ShoppingGoodsAdapter.ShoppingGoodsViewHolder> {

    private BitmapTools mBitmapTools;
    public boolean[] isCheckedArray;
    public CheckClickListener mCheckClickListener;
    public HashMap<Integer, Boolean> isSelected;

    public ShoppingGoodsAdapter(Context context, List<ShoppingEntity.ShoppingCarEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
        isSelected = new HashMap<Integer, Boolean>();

    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < mItems.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public ShoppingGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShoppingGoodsViewHolder(mInflater.inflate(R.layout.item_shopping_goods, parent, false));

    }

    @Override
    public void onBindViewHolder(final ShoppingGoodsViewHolder holder, final int position) {
        final ShoppingEntity.ShoppingCarEntity item = getItem(position);
        holder.mTv_goods_name.setText(item.goods_name);
        holder.mTv_goods_price.setText(item.price);
        if (item.goods_image != null && item.goods_image.size() > 0) {
            mBitmapTools.display(holder.mIv_goods_head, UrlConst.IMAGE_URL + item.goods_image.get(0));
        }
        boolean b = getIsSelected().get(position) == null ? false : getIsSelected().get(position);

        holder.mCb_check.setChecked(b);
        holder.mCb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();

                getIsSelected().put(pos, holder.mCb_check.isChecked());
                if (holder.mCb_check.isChecked()) {
                    isCheckedArray[pos] = true;
                    mCheckClickListener.onCheckClickListener(isCheckedArray);
                } else {
                    isCheckedArray[pos] = false;
                    mCheckClickListener.onCheckClickListener(isCheckedArray);
                }
            }
        });
        try {
            String s = TextUtils.isEmpty(item.stock) ? "1" : item.stock;
            holder.mAv_add_subtract.setGoods_storage(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {


            holder.mAv_add_subtract.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    item.num = amount + "";
                    int pos = holder.getLayoutPosition();
                    if (holder.mCb_check.isChecked()) {
                        isCheckedArray[pos] = true;
                        mCheckClickListener.onCheckClickListener(isCheckedArray);
                    } else {
                        isCheckedArray[pos] = false;
                        mCheckClickListener.onCheckClickListener(isCheckedArray);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mAv_add_subtract.setAmount(TextUtils.isEmpty(item.num) ? 1 : Integer.parseInt(item.num));
    }

    public void setItmeSize(List<ShoppingEntity.ShoppingCarEntity> list) {
        isCheckedArray = new boolean[list.size()];
        initDate();
    }

    class ShoppingGoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_goods_head;
        private final CheckBox mCb_check;
        private final TextView mTv_goods_name;
        private final TextView mTv_goods_price;
        private final AmountView mAv_add_subtract;

        public ShoppingGoodsViewHolder(View itemView) {
            super(itemView);
            mCb_check = itemView.findViewById(R.id.cb_check);
            mIv_goods_head = itemView.findViewById(R.id.iv_goods_head);
            mTv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            mTv_goods_price = itemView.findViewById(R.id.tv_goods_price);
            mAv_add_subtract = itemView.findViewById(R.id.av_add_subtract);
        }
    }

    public void setCheckClickListener(CheckClickListener listener) {
        mCheckClickListener = listener;
    }
}
