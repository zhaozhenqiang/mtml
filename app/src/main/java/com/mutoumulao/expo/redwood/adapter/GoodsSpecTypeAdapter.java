package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.RecyclerViewAddItemListener;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.util.List;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecTypeAdapter extends BaseAdapter<StoreManagerListEntity.GuigesEntity, GoodsSpecTypeAdapter.GoodsSpecTypeViewHolder> {

    //    private List<List<String>> mTypeList = new ArrayList<>();
    private RecyclerViewAddItemListener mAddItemListener;
    public GoodsSpecTypeInfoAdapter mInfoAdapter;

    public GoodsSpecTypeAdapter(Context context, List<StoreManagerListEntity.GuigesEntity> list) {
        super(context, list);
    }

    @Override
    public GoodsSpecTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsSpecTypeViewHolder(mInflater.inflate(R.layout.item_goods_spec_type, null));
    }

    @Override
    public void onBindViewHolder(final GoodsSpecTypeViewHolder holder, final int position) {
        final StoreManagerListEntity.GuigesEntity item = getItem(position);
        holder.mTv_type_name.setText(item.title);
/*
        mInfoAdapter = new GoodsSpecTypeInfoAdapter(mContext, item.guigeArray, position);
        holder.mRv_spec_size.setAdapter(mInfoAdapter);

        mInfoAdapter.setOnItemDeleteListener(new ImageRecyclerReduceItemListener() {
            @Override
            public void onReduceItemListener(int position) {
                   */
/* item.info_name.remove(position);
                    mInfoAdapter.notifyDataSetChanged();*//*

                List<String> list = bindAnotherRecyler1();
                mAddItemListener.onAddItemListener(list, position);
            }
        });

        holder.mEt_size.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String size = holder.mEt_size.getText().toString().trim();
                    if (!TextUtils.isEmpty(size)) {
                        mInfoAdapter = new GoodsSpecTypeInfoAdapter(mContext, item.guigeArray */
/*mTypeList*//*
, position);
                        item.guigeArray.add(size);

//                        List<String> list = bindAnotherRecyler();
                        List<String> list = bindAnotherRecyler1();
//                        mTypeList.add(item.info_name);
                        holder.mRv_spec_size.setAdapter(mInfoAdapter);
                        mAddItemListener.onAddItemListener(list, position);
                        holder.mEt_size.setText("");

                        mInfoAdapter.setOnItemDeleteListener(new ImageRecyclerReduceItemListener() {
                            @Override
                            public void onReduceItemListener(int position) {

                                List<String> list = bindAnotherRecyler1();
                                mAddItemListener.onAddItemListener(list, position);
                            }
                        });
                    } else {
                        UIUtil.toastShort(mContext, "请输入规格参数");
                    }
                }
                return false;
            }
        });
*/


    }

    private List<String> bindAnotherRecyler1() {
        int b = 0;
/*        if (mItems.size() > 0) {
            List<String> copylist = new ArrayList<>();
            for (int a = 0; a < mItems.size(); a++) {
                if (mItems.get(a).guigeArray.size() != 0) {
                    copylist.addAll(mItems.get(a).guigeArray);
                    b = a;
                    break;
                }
            }
            if (copylist.size() > 0) {
                List<String> L0 = new ArrayList<>();
                L0.addAll(copylist);
                for (int i = b + 1; i < mItems.size(); i++) {
                    List<String> L1 = mItems.get(i).guigeArray;

                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < L0.size(); j++) {
                        for (int z = 0; z < L1.size(); z++) {
                            String s = L0.get(j) + ":" + L1.get(z);
                            list.add(s);
                        }
                    }
                    if (list.size() != 0) {
                        L0 = list;
                    }
                }
                return L0;
            }
        }*/
        return null;
    }

    private List<String> bindAnotherRecyler() {
/*        if (mItems.size() > 0) {

            List<String> L0 = mItems.get(0).guigeArray;
            for (int i = 1; i < mItems.size(); i++) {
                List<String> L1 = mItems.get(i).guigeArray;

                List<String> list = new ArrayList<>();
                for (int j = 0; j < L0.size(); j++) {
                    for (int z = 0; z < L1.size(); z++) {
                        String s = L0.get(j) + " + " + L1.get(z);
                        list.add(s);
                    }
                }
                L0 = list;
            }
            return L0;
        }*/
        return null;
    }

    class GoodsSpecTypeViewHolder extends RecyclerView.ViewHolder {

        private final BaseRecyclerView mRv_spec_size;
        private final EditText mEt_size;
        private final TextView mTv_type_name;

        public GoodsSpecTypeViewHolder(View itemView) {
            super(itemView);
            mTv_type_name = itemView.findViewById(R.id.tv_type_name);
            mEt_size = itemView.findViewById(R.id.et_size);
            mRv_spec_size = itemView.findViewById(R.id.rv_spec_size);
        }
    }

    public void setAddItem(RecyclerViewAddItemListener addItemListener) {
        mAddItemListener = addItemListener;
    }


}
