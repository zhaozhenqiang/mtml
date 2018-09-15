package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecylerReduceItemListener;
import com.mutoumulao.expo.redwood.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecSelfAdapter extends RecyclerView.Adapter<GoodsSpecSelfAdapter.GoodsSpecTypeViewHolder> {
    private final Context mContext;
    private List<String> getItem;
    //    private final List<List<String>> getItem;
    protected LayoutInflater mInflater;
    private int mPosition;
    private ImageRecylerReduceItemListener mItemDeleteListener;
//    private List<GoodsSpecTypeEntity> mList = new ArrayList<>();

    public GoodsSpecSelfAdapter(Context context, /*List<List<String>>*/List<String> list, int position) {
        mContext = context;
        getItem = list;
        mInflater = LayoutInflater.from(context);
        mPosition = position;
    }

    @Override
    public GoodsSpecTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsSpecTypeViewHolder(mInflater.inflate(R.layout.item_goods_spec_self, parent, false));
    }

    @Override
    public void onBindViewHolder(final GoodsSpecTypeViewHolder holder, final int position) {
        holder.mRl_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem.remove(position);
                notifyDataSetChanged();
                mItemDeleteListener.onReduceItemListener(position);
            }
        });
        if(getItem==null || position == getItem.size()){
            holder.mEt.setVisibility(View.VISIBLE);
            holder.mRl.setVisibility(View.GONE);
        }else{
            holder.mEt.setVisibility(View.GONE);
            holder.mRl.setVisibility(View.VISIBLE);
            String item = getItem/*.get(mPosition)*/.get(position);
            if (!TextUtils.isEmpty(item)) {
                holder.mTv_size.setVisibility(View.VISIBLE);
                holder.mTv_size.setText(item);
            } else {
                holder.mTv_size.setVisibility(View.GONE);
            }
        }

        holder.mEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String size = holder.mEt.getText().toString().trim();
                    if (!TextUtils.isEmpty(size)) {
                        if(getItem==null)
                            getItem = new ArrayList<>();
                        getItem.add(size);
                        notifyDataSetChanged();
                    } else {
                        UIUtil.toastShort(mContext, "请输入规格参数");
                    }
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        try {
            if (getItem != null && getItem.size() > 0) {
                return getItem./*get(mPosition).*/size()+1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    class GoodsSpecTypeViewHolder extends RecyclerView.ViewHolder {


        private final EditText mEt;
        private final RelativeLayout mRl;

        private final TextView mTv_size;
        private final RelativeLayout mRl_del;
        private final ImageView mIv_del;

        public GoodsSpecTypeViewHolder(View itemView) {
            super(itemView);
            mEt = itemView.findViewById(R.id.et_spec_self);
            mRl = itemView.findViewById(R.id.rl_content);
            mTv_size = itemView.findViewById(R.id.tv_size);
            mRl_del = itemView.findViewById(R.id.rl_del);
            mIv_del = itemView.findViewById(R.id.iv_del);
        }
    }

    public void setOnItemDeleteListener(ImageRecylerReduceItemListener itemDeleteListener) {
        mItemDeleteListener = itemDeleteListener;
    }

}
