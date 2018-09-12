package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.OrderdetailActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.OrderEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.OrderListLeftRightListener;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/22.
 */

public class OrderListAdapter extends BaseAdapter<OrderEntity, OrderListAdapter.OrderListViewHolder> {

    private List<OrderEntity.ListEntity> mGoodsList = new ArrayList<>();
    public OrderGoodsAdapter mAdapter;
    private BitmapTools mBitmapTools;
    private final String mIs_business;//1 是商户  0 普通用户
    private OrderListLeftRightListener mLeftRightClickListener;

    public OrderListAdapter(Context context, List<OrderEntity> list, String is_business) {
        super(context, list);
        mAdapter = new OrderGoodsAdapter(context, mGoodsList);
        mBitmapTools = new BitmapTools(context);
        mIs_business = is_business;
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderListViewHolder(mInflater.inflate(R.layout.item_order_list, parent, false));

    }

    @Override
    public void onBindViewHolder(OrderListViewHolder holder, final int position) {
        final OrderEntity item = getItem(position);
        mBitmapTools.display(holder.mIv_store_head, UrlConst.IMAGE_URL + item.avatar);
        holder.mTv_store_name.setText(item.business_name);
        if ("0".equals(item.status)) {
            holder.mTv_status.setText("待付款");
            if ("0".equals(mIs_business)) {
                holder.mLl_button.setVisibility(View.VISIBLE);
                holder.mTv_left.setVisibility(View.VISIBLE);
                holder.mTv_right.setVisibility(View.VISIBLE);
                holder.mTv_left.setText("取消");
                holder.mTv_right.setText("支付");
            } else {
                holder.mLl_button.setVisibility(View.GONE);
            }

        } else if ("1".equals(item.status)) {
            holder.mTv_status.setText("待发货");
            if ("0".equals(mIs_business)) {
                holder.mLl_button.setVisibility(View.GONE);
            } else {
                holder.mLl_button.setVisibility(View.VISIBLE);
                holder.mTv_left.setVisibility(View.GONE);
                holder.mTv_right.setVisibility(View.VISIBLE);
                holder.mTv_right.setText("填单发货");
            }
        } else if ("4".equals(item.status)) {
            holder.mTv_status.setText("已发货");
            if ("0".equals(mIs_business)) {
                holder.mLl_button.setVisibility(View.VISIBLE);
                holder.mTv_left.setVisibility(View.VISIBLE);
                holder.mTv_right.setVisibility(View.VISIBLE);
                holder.mTv_left.setText("确认收货");
                holder.mTv_right.setText("查看物流");
            } else {
                holder.mLl_button.setVisibility(View.VISIBLE);
                holder.mTv_left.setVisibility(View.GONE);
                holder.mTv_right.setVisibility(View.VISIBLE);
                holder.mTv_right.setText("查看物流");
            }
        } else if ("5".equals(item.status)) {
            holder.mTv_status.setText("已签收");
            if ("0".equals(mIs_business)) {
                holder.mLl_button.setVisibility(View.VISIBLE);
                holder.mTv_left.setVisibility(View.GONE);
                holder.mTv_right.setVisibility(View.VISIBLE);
                holder.mTv_right.setText("评价订单");
            } else {
                holder.mLl_button.setVisibility(View.GONE);
            }

        } else if ("7".equals(item.status)) {
            holder.mTv_status.setText("已完成");
            holder.mLl_button.setVisibility(View.GONE);

        } else if ("8".equals(item.status)) {
            holder.mTv_status.setText("已取消");
            holder.mLl_button.setVisibility(View.GONE);
        }
        List<OrderEntity.ListEntity> good_list = item.list;
        double all_price = 0.0;
        for (int i = 0; i < good_list.size(); i++) {
            String num = TextUtils.isEmpty(good_list.get(i).num) ? "0" : good_list.get(i).num;
            String price = TextUtils.isEmpty(good_list.get(i).sku_info.price) ? "0" : good_list.get(i).sku_info.price;
            all_price = all_price + (Double.parseDouble(num) * Double.parseDouble(price));
        }

        holder.mTv_desc.setText("共" + good_list.size() + "件商品 合计" + all_price + "元 (运费：" + item.freight + "元）");
        mGoodsList.clear();
        if (good_list != null) {
            mGoodsList.addAll(good_list);
            holder.mRv_goods.setAdapter(mAdapter);
        }

        holder.mTv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftRightClickListener.rightClick(position, item.status);
            }
        });
        holder.mTv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftRightClickListener.leftClick(position, item.status);
            }
        });
        holder.mRv_goods.setOnItemLongClickListener(new BaseRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
                return false;
            }
        });

        holder.mRv_goods.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, OrderdetailActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("is_business", mIs_business);
                mContext.startActivity(intent);
            }
        });

    }

    class OrderListViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_store_head;
        private final TextView mTv_store_name;
        private final BaseRecyclerView mRv_goods;
        private final TextView mTv_desc;
        private final LinearLayout mLl_button;
        private final TextView mTv_left;
        private final TextView mTv_right;
        private final TextView mTv_status;

        public OrderListViewHolder(View itemView) {
            super(itemView);
            mIv_store_head = itemView.findViewById(R.id.iv_store_head);
            mTv_store_name = itemView.findViewById(R.id.tv_store_name);
            mRv_goods = itemView.findViewById(R.id.rv_goods);
            mTv_desc = itemView.findViewById(R.id.tv_desc);
            mLl_button = itemView.findViewById(R.id.ll_button);
            mTv_left = itemView.findViewById(R.id.tv_left);
            mTv_right = itemView.findViewById(R.id.tv_right);
            mTv_status = itemView.findViewById(R.id.tv_status);
        }
    }

    public void setLeftRightClickListener(OrderListLeftRightListener leftRightClickListener) {
        mLeftRightClickListener = leftRightClickListener;
    }
}
