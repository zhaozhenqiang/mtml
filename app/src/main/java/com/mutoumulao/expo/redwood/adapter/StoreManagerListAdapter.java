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
import com.mutoumulao.expo.redwood.activity.GoodsDetialActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;

import java.util.List;

/**
 * Created by lzy on 2018/8/8.
 */

public class StoreManagerListAdapter extends BaseAdapter<StoreManagerListEntity, StoreManagerListAdapter.StoreManagerListViewHolder> {

    private final BitmapTools mBitmapTools;
    private boolean showFlag;

    public StoreManagerListAdapter(Context context, List<StoreManagerListEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
    }
    public StoreManagerListAdapter(Context context, List<StoreManagerListEntity> list,boolean showFlag) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
        this.showFlag = showFlag;
    }

    @Override
    public StoreManagerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreManagerListViewHolder(mInflater.inflate(R.layout.item_store_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreManagerListViewHolder holder, final int position) {
        try {
            final StoreManagerListEntity entity = mItems.get(position);
            List<String> goods_image = entity.goods_image;
            if (goods_image.size() > 0) {
                mBitmapTools.display(holder.mIv_goods, UrlConst.IMAGE_URL + goods_image.get(0));
            }
            holder.mTv_name.setText(entity.goods_name);
            String sale_num = TextUtils.isEmpty(entity.sale_num) ? "0" : entity.sale_num;
            holder.mTv_location.setText("销量：" + sale_num);
            holder.mTv_price.setText(TextUtils.isEmpty(entity.minPrice) ? entity.price : entity.minPrice);
            holder.mTv_other.setText(entity.send_address);
            holder.mLl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, GoodsDetialActivity.class).putExtra("id", entity.id));
                }
            });

            if(showFlag){
                holder.mTvEdit.setVisibility(View.VISIBLE);
                holder.mTvDelete.setVisibility(View.VISIBLE);
            }else {
                holder.mTvEdit.setVisibility(View.GONE);
                holder.mTvDelete.setVisibility(View.GONE);
            }

            holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(position,view);
                }
            });
            holder.mTvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(position,view);
                }
            });

            holder.mLl_content.setTag(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class StoreManagerListViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv_goods;
        private final TextView mTv_name;
        private final TextView mTv_location;
        private final TextView mTv_price;
        private final TextView mTv_other;
        private final LinearLayout mLl_content;

        private final TextView mTvEdit;
        private final TextView mTvDelete;


        public StoreManagerListViewHolder(View itemView) {
            super(itemView);
            mIv_goods = itemView.findViewById(R.id.iv_goods);
            mTv_name = itemView.findViewById(R.id.tv_name);
            mTv_location = itemView.findViewById(R.id.tv_location);
            mTv_price = itemView.findViewById(R.id.tv_price);
            mTv_other = itemView.findViewById(R.id.tv_other);
            mLl_content = itemView.findViewById(R.id.ll_content);
            mTvEdit = itemView.findViewById(R.id.tv_edit);
            mTvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }
    GridImageAdapter.OnItemClickListener clickListener;

    public GridImageAdapter.OnItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(GridImageAdapter.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
