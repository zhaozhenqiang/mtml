package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.GoodsSpecActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecTypeNumberAdapter extends BaseAdapter<StoreManagerListEntity.SkuListEntity, GoodsSpecTypeNumberAdapter.GoodsSpecTypeViewHolder> {
    private final DecimalFormat mFormat;

    public GoodsSpecTypeNumberAdapter(Context context, List<StoreManagerListEntity.SkuListEntity> list) {
        super(context, list);
        mFormat = new DecimalFormat("##.##");
    }

    @Override
    public GoodsSpecTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsSpecTypeViewHolder(mInflater.inflate(R.layout.item_goods_spec_number, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsSpecTypeViewHolder holder, int position) {
        StoreManagerListEntity.SkuListEntity item = getItem(position);


        holder.mEt_number_price.setTag(position * 100 + 1);
        holder.mEt_number_content.setTag(position * 100 + 2);

        holder.mTv_number_name.setText(item.spec);
        if (!TextUtils.isEmpty(item.stock)) {
            holder.mEt_number_content.setText(item.stock);
        }else {
            holder.mEt_number_content.setText("");
        }
        if (!TextUtils.isEmpty(item.price)) {
            holder.mEt_number_price.setText(item.price);
        }else {
            holder.mEt_number_price.setText("");
        }

        holder.mEt_number_price.addTextChangedListener(new TextSwitcher(holder, "1"));
        holder.mEt_number_content.addTextChangedListener(new TextSwitcher(holder, "2"));
    }

    class GoodsSpecTypeViewHolder extends RecyclerView.ViewHolder {


        private final TextView mTv_number_name;
        private final EditText mEt_number_price;
        private final EditText mEt_number_content;

        public GoodsSpecTypeViewHolder(View itemView) {
            super(itemView);
            mTv_number_name = itemView.findViewById(R.id.tv_number_name);
            mEt_number_price = itemView.findViewById(R.id.et_number_price);
            mEt_number_content = itemView.findViewById(R.id.et_number_content);
        }
    }

    class TextSwitcher implements TextWatcher {
        private GoodsSpecTypeViewHolder mHolder;
        private String mType;

        public TextSwitcher(GoodsSpecTypeViewHolder mHolder, String type) {
            this.mHolder = mHolder;
            mType = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if ("1".equals(mType)) {
                int position = (Integer) mHolder.mEt_number_price.getTag();
                ((GoodsSpecActivity) mContext).saveEditData(position, mType, s.toString());
            } else if ("2".equals(mType)) {
                int position = (Integer) mHolder.mEt_number_content.getTag();
                ((GoodsSpecActivity) mContext).saveEditData(position, mType, s.toString());
            }
        }
    }
}
