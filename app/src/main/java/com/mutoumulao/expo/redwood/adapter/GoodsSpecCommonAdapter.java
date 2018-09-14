package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecCommonAdapter extends RecyclerView.Adapter<GoodsSpecCommonAdapter.GoodsSpecTypeViewHolder> {
    private final Context mContext;
    protected LayoutInflater mInflater;
    private ImageRecyclerReduceItemListener mItemDeleteListener;
    /*
    @"18",@"19",@"20",@"21",@"22",@"23",@"24",@"25",@"26",@"27",@"28"

    @"尺寸",@"材质",@"型号",@"颜色",@"款式",
    @"器型",@"口味",@"色号",@"适用人群",@"容量",
    @"花型",@"尺码",@"地点",@"香型",@"货号",
    @"组合",@"成份",@"版本",@"度数",@"运营商",
    @"属性",@"重量",@"地区",@"套餐",@"类别",
    @"适用年龄",@"功效",@"品类",@"时间"
    * */

    private String[] typeArray2 = {"18", "19", "20", "21", "22", "23", "24", "25", "26","27","28"};
    private String[] typeArray1 = {"尺寸", "材质", "型号", "颜色", "款式",
            "器型", "口味", "色号", "适用人群", "容量",
            "花型", "尺码", "地点", "香型", "货号",
            "组合", "成份", "版本", "度数", "运营商",
            "属性", "重量", "地区", "套餐", "类别",
            "适用年龄", "功效", "品类", "时间"};
    boolean[] typeFlag;

    public GoodsSpecCommonAdapter(Context context, boolean type) {
        mContext = context;
        if(true){
            typeFlag = new boolean[typeArray1.length];
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public GoodsSpecTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsSpecTypeViewHolder(mInflater.inflate(R.layout.item_goods_spec_common, parent, false));
    }

    @Override
    public void onBindViewHolder(final GoodsSpecTypeViewHolder holder, final int position) {
        holder.mTv_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeFlag[position] = !typeFlag[position];
                notifyDataSetChanged();
                //mItemDeleteListener.onReduceItemListener(position);
            }
        });
        holder.mTv_size.setText(typeArray1[position]);
        if(typeFlag[position]){
            holder.mTv_size.setTextColor(Color.RED);
            holder.mRl.setEnabled(true);
        }else {
            holder.mRl.setEnabled(false);
            holder.mTv_size.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return typeFlag.length;
    }

    class GoodsSpecTypeViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_size;
        private final View mRl;

        public GoodsSpecTypeViewHolder(View itemView) {
            super(itemView);
            mTv_size = itemView.findViewById(R.id.tv_size);
            mRl = itemView.findViewById(R.id.rl_content);
        }
    }

    public void setOnItemDeleteListener(ImageRecyclerReduceItemListener itemDeleteListener) {
        mItemDeleteListener = itemDeleteListener;
    }
}
