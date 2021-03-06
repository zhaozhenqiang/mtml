package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;
import com.mutoumulao.expo.redwood.entity.custom_interface.RecyclerViewAddOneListener;

import java.util.List;

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

    /*
    乳白色  蓝色 黑色  白色 青色
    银色  灰色  桔红色 西瓜红 粉红色
    明黄色  荧光黄  金色 墨绿色 米白色
    银色 深灰色 玫红色 红色 卡其色
    杏色 香横色 浅黄色 翠绿色 绿色
    天蓝色 花色 米黄色
    *
    * */

    private String[] typeArray2 = {"18", "19", "20", "21", "22", "23", "24", "25", "26","27","28"};
    private String[] typeArray1 = {"乳白色", "蓝色", "黑色", "白色", "青色",
            "银色", "灰色", "桔红色", "西瓜红", "粉红色",
            "明黄色", "荧光黄", "金色", "墨绿色", "米白色",
            "银色", "深灰色", "玫红色", "红色", "卡其色",
            "杏色", "香横色", "浅黄色", "翠绿色", "绿色",
            "天蓝色", "花色", "米黄色"};//, "时间"};
    boolean[] typeFlag = new boolean[typeArray1.length];
    public boolean moreFlag;
    //private List<StoreManagerListEntity.GuigesEntity> mSpecNameCommonList = new ArrayList<>();

    public GoodsSpecCommonAdapter(Context context, List<StoreManagerListEntity.GuigesEntity> list) {
        mContext = context;
        //this.mSpecNameCommonList = list;
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                for(int j=0;j<typeArray1.length;j++) {
                    if (list.get(i).title.equals(typeArray1[j])){
                        typeFlag[j] = true;
                        continue;
                    }
                }
            }
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
                if(!typeFlag[position])
                    mItemDeleteListener.onReduceItemListener(position);
                else
                    mAddItemListener.onAddItemListener(null,position);

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
        if(moreFlag)
            return typeFlag.length;
        else
            return 6;
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
    private RecyclerViewAddOneListener mAddItemListener;

    public void setAddItem(RecyclerViewAddOneListener addItemListener) {
        mAddItemListener = addItemListener;
    }

}
