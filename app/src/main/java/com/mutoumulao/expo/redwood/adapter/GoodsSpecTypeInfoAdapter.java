package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;

import java.util.List;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecTypeInfoAdapter extends RecyclerView.Adapter<GoodsSpecTypeInfoAdapter.GoodsSpecTypeViewHolder> {
    private final Context mContext;
    private final List<String> getItem;
    //    private final List<List<String>> getItem;
    protected LayoutInflater mInflater;
    private int mPosition;
    private ImageRecyclerReduceItemListener mItemDeleteListener;
//    private List<GoodsSpecTypeEntity> mList = new ArrayList<>();

    public GoodsSpecTypeInfoAdapter(Context context, /*List<List<String>>*/List<String> list, int position) {
        mContext = context;
        getItem = list;
        mInflater = LayoutInflater.from(context);
        mPosition = position;
    }

    @Override
    public GoodsSpecTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsSpecTypeViewHolder(mInflater.inflate(R.layout.item_goods_spec_info, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsSpecTypeViewHolder holder, final int position) {
        holder.mRl_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getItem.remove(position);
                notifyDataSetChanged();
                mItemDeleteListener.onReduceItemListener(position);
            }
        });
        String item = getItem/*.get(mPosition)*/.get(position);
        if (!TextUtils.isEmpty(item)) {
            holder.mTv_size.setVisibility(View.VISIBLE);
            holder.mTv_size.setText(item);
        } else {
            holder.mTv_size.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        try {
            if (getItem != null && getItem.size() > 0) {
                return getItem./*get(mPosition).*/size();
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    class GoodsSpecTypeViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_size;
        private final RelativeLayout mRl_del;
        private final ImageView mIv_del;

        public GoodsSpecTypeViewHolder(View itemView) {
            super(itemView);
            mTv_size = itemView.findViewById(R.id.tv_size);
            mRl_del = itemView.findViewById(R.id.rl_del);
            mIv_del = itemView.findViewById(R.id.iv_del);
        }
    }

    public void setOnItemDeleteListener(ImageRecyclerReduceItemListener itemDeleteListener) {
        mItemDeleteListener = itemDeleteListener;
    }

}
