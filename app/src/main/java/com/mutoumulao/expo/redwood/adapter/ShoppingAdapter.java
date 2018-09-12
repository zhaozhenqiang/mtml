package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.GoodsDetialActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.InnerRecylerViewListener;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/9.
 */

public class ShoppingAdapter extends BaseAdapter<ShoppingEntity, ShoppingAdapter.ShoppingViewHolder> {

    private BitmapTools mBitmapTools;
    private List<ShoppingEntity.ShoppingCarEntity> mGoodsList = new ArrayList<>();
    public ShoppingGoodsAdapter mAdapter;
    private InnerRecylerViewListener mRecylerViewClickView;

    public ShoppingAdapter(Context context, List<ShoppingEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
        mAdapter = new ShoppingGoodsAdapter(context, mGoodsList);
    }

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShoppingViewHolder(mInflater.inflate(R.layout.item_shopping_car, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, final int position) {
        final ShoppingEntity item = getItem(position);
        mBitmapTools.display(holder.mIv_store_head, UrlConst.IMAGE_URL + item.avatar);
        holder.mTv_store_name.setText(item.business_name);
        mGoodsList.clear();
        mGoodsList.addAll(item.list);
        mAdapter.setItmeSize(item.list);
        holder.mRv_goods.setAdapter(mAdapter);
        holder.mRv_goods.setOnItemLongClickListener(new BaseRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View view,final int position1, long id) {
                mRecylerViewClickView.onReduceItemListener(position,position1);
                return false;
            }
        });
        holder.mRv_goods.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position2, long id) {
                Intent intent = new Intent(mContext, GoodsDetialActivity.class);
                intent.putExtra("id",item.list.get(position2).goods_id);
                mContext.startActivity(intent);
            }
        });

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

    public void setOnInnerRecylerViewClickView(InnerRecylerViewListener recylerViewClickView){

        mRecylerViewClickView = recylerViewClickView;
    }

}
