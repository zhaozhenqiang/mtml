package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.custom_interface.SKUInterface;
import com.mutoumulao.expo.redwood.view.SKUViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/18.
 */

public class GoodsDetialSpecAdapter extends BaseAdapter<GoodsDetialEntity.GuigesEntity, GoodsDetialSpecAdapter.GoodsDetialSpecViewHolder1> {
    private SKUInterface myInterface;
    private TextView[][] childrenViews;    //二维 装所有属性
    private SimpleArrayMap<Integer, String> saveClick;
    private String[] selectedValue;   //选中的属性
    private final int SELECTED = 0x100;
    private final int CANCEL = 0x101;

    public GoodsDetialSpecAdapter(Context context, List<GoodsDetialEntity.GuigesEntity> list) {
        super(context, list);
        childrenViews = new TextView[list.size()][0];
        saveClick = new SimpleArrayMap<>();
        selectedValue = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            selectedValue[i] = "";
        }
    }

    @Override
    public GoodsDetialSpecViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsDetialSpecViewHolder1(mInflater.inflate(R.layout.item_skuattrs, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsDetialSpecViewHolder1 holder, int position) {
        try {
            GoodsDetialEntity.GuigesEntity item = getItem(position);
            holder.tv_ItemName.setText(item.title);
            List<String> childrens = item.guigeArray;
            int childrenSize = childrens.size();
            TextView[] textViews = new TextView[childrenSize];

            for (int i = 0; i < childrenSize; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 15, 10);
                TextView textView = new TextView(mContext);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(15, 5, 15, 5);
                textView.setLayoutParams(params);
                textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_gray_fill));
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.tc_black3));
                textView.setText(childrens.get(i));
//            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                textViews[i] = textView;
                holder.vg_skuItem.addView(textViews[i]);
            }
            childrenViews[position] = textViews;
            initOptions();
            canClickOptions();

            getSelected();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int focusPositionG, focusPositionC;

    private class MyOnClickListener implements View.OnClickListener {
        //点击操作 选中SELECTED   取消CANCEL
        private int operation;

        private int positionG;

        private int positionC;

        public MyOnClickListener(int operation, int positionG, int positionC) {
            this.operation = operation;
            this.positionG = positionG;
            this.positionC = positionC;
        }

        @Override
        public void onClick(View v) {
            focusPositionG = positionG;
            focusPositionC = positionC;
            String value = childrenViews[positionG][positionC].getText().toString();
            switch (operation) {
                case SELECTED:
                    saveClick.put(positionG, positionC + "");
                    selectedValue[positionG] = value;
                    myInterface.selectedAttribute(selectedValue);
                    break;
                case CANCEL:
                    saveClick.put(positionG, "");
                    for (int l = 0; l < selectedValue.length; l++) {
                        if (selectedValue[l].equals(value)) {
                            selectedValue[l] = "";
                            break;
                        }
                    }
                    myInterface.uncheckAttribute(selectedValue);
                    break;
            }
            initOptions();
            canClickOptions();
            getSelected();
        }
    }

    class MyOnFocusChangeListener implements View.OnFocusChangeListener {

        private int positionG;

        private int positionC;


        public MyOnFocusChangeListener(int positionG, int positionC) {
            this.positionG = positionG;
            this.positionC = positionC;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            String clickpositionC = saveClick.get(positionG);
            if (hasFocus) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tc_black9));
                if (TextUtils.isEmpty(clickpositionC)) {
                    ((TextView) v).setTextColor(ContextCompat.getColor(mContext, R.color.bc_blue));
                } else if (clickpositionC.equals(positionC + "")) {

                } else {
                    ((TextView) v).setTextColor(ContextCompat.getColor(mContext, R.color.bc_blue));
                }
            } else {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tc_black9));
                if (TextUtils.isEmpty(clickpositionC)) {
                    ((TextView) v).setTextColor(ContextCompat.getColor(mContext, R.color.white));
                } else if (clickpositionC.equals(positionC + "")) {

                } else {
                    ((TextView) v).setTextColor(ContextCompat.getColor(mContext, R.color.white));
                }
            }
        }

    }

    /**
     * 初始化选项（不可点击，焦点消失）
     */
    private void initOptions() {
        for (int y = 0; y < childrenViews.length; y++) {
            for (int z = 0; z < childrenViews[y].length; z++) {//循环所有属性
                TextView textView = childrenViews[y][z];
                textView.setEnabled(false);
                textView.setFocusable(false);
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.tc_black9));//变灰
            }
        }
    }

/*    */

    /**
     * 找到符合条件的选项变为可选
     */
    private void canClickOptions() {
        try {


            List<String> list = new ArrayList<>();
            for (int z = 0; z < mItems.size(); z++) {
                for (int x = 0; x < mItems.get(z).guigeArray.size(); x++) {
                    list.add(mItems.get(z).guigeArray.get(x));
                }
            }
            for (int i = 0; i < childrenViews.length; i++) {
                for (int j = 0; j < list.size(); j++) {
                    boolean filter = false;
                /*List<String> goodsInfo = new ArrayList<>();
                goodsInfo.addAll(list);*/
                /*for (int k = 0; k < selectedValue.length; k++) {
                    if (i == k || TextUtils.isEmpty(selectedValue[k])) {
                        continue;
                    }
                    if (!selectedValue[k].equals(goodsInfo
                            .get(k)*//*.getTabValue()*//*)) {
                        filter = true;
                        break;
                    }
                }*/
                    if (!filter) {
                        for (int n = 0; n < childrenViews[i].length; n++) {
                            TextView textView = childrenViews[i][n];//拿到所有属性TextView
                            String name = textView.getText().toString();
                            //拿到属性名称
                            if (list.get(j)./*getTabValue().*/equals(name)) {
                                textView.setEnabled(true);//符合就变成可点击
                                textView.setFocusable(true); //设置可以获取焦点
                                //不要让焦点乱跑
                                if (focusPositionG == i && focusPositionC == n) {
                                    textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_gray_fill));
                                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.tc_black3));
                                    textView.requestFocus();
                                } else {
                                    textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_gray_fill));
                                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.tc_black3));
                                }
                                textView.setOnClickListener(new MyOnClickListener(SELECTED, i, n) {
                                });
                                textView.setOnFocusChangeListener(new MyOnFocusChangeListener(i, n) {
                                });
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 找到已经选中的选项，让其变红
     */
    private void getSelected() {
        for (int i = 0; i < childrenViews.length; i++) {
            for (int j = 0; j < childrenViews[i].length; j++) {//拿到每行属性Item
                TextView textView = childrenViews[i][j];//拿到所有属性TextView
                String value = textView.getText().toString();
                for (int m = 0; m < selectedValue.length; m++) {
                    if (selectedValue[m].equals(value)) {
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.tc_write));
                        textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_red_fill));
                        textView.setOnClickListener(new MyOnClickListener(CANCEL, i, j) {

                        });
                    }
                }
            }
        }
    }

    class GoodsDetialSpecViewHolder1 extends RecyclerView.ViewHolder {

        private TextView tv_ItemName;
        private SKUViewGroup vg_skuItem;

        public GoodsDetialSpecViewHolder1(View itemView) {
            super(itemView);
            tv_ItemName = itemView.findViewById(R.id.tv_ItemName);
            vg_skuItem = itemView.findViewById(R.id.vg_skuItem);
        }
    }

    public void setSKUInterface(SKUInterface myInterface) {
        this.myInterface = myInterface;
    }

}
