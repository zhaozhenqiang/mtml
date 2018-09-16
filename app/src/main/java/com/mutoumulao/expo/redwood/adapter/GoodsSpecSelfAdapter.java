package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;
import com.mutoumulao.expo.redwood.entity.custom_interface.RecyclerViewAddOneListener;
import com.mutoumulao.expo.redwood.util.UIUtil;

import java.util.List;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecSelfAdapter extends RecyclerView.Adapter<GoodsSpecSelfAdapter.GoodsSpecTypeViewHolder> {
    private final Context mContext;
    private List<StoreManagerListEntity.GuigesEntity> getItem;
    protected LayoutInflater mInflater;
    


    public GoodsSpecSelfAdapter(Context context,List<StoreManagerListEntity.GuigesEntity> list) {
        mContext = context;
        getItem = list;
        mInflater = LayoutInflater.from(context);
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
/*                getItem.remove(position);
                notifyDataSetChanged();*/
                mItemDeleteListener.onReduceItemListener(position);
            }
        });
        holder.mTv_size.removeTextChangedListener(holder.attTextWatcher);
        if (getItem == null || position == getItem.size()) {
            holder.mTv_size.setText("");
            holder.mIv_del.setVisibility(View.GONE);
            holder.mRl_del.setVisibility(View.GONE);
        } else {
            holder.mRl_del.setVisibility(View.VISIBLE);
            holder.mIv_del.setVisibility(View.VISIBLE);
            holder.mIv_del.setVisibility(View.VISIBLE);
            String item = getItem.get(position).title;
            if (!TextUtils.isEmpty(item)) {
                holder.mTv_size.setText(item);
                holder.mTv_size.setSelection(item.length());
            } else {
                holder.mTv_size.setText("");
            }
        }
        holder.mTv_size.setTag(position);
        holder.mTv_size.addTextChangedListener(holder.attTextWatcher);

/*        holder.mTv_size.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String size = holder.mTv_size.getText().toString().trim();
                    if (!TextUtils.isEmpty(size)) {
*//*                        if (getItem == null)
                            getItem = new ArrayList<>();
                        StoreManagerListEntity.GuigesEntity bean = new StoreManagerListEntity.GuigesEntity();
                        bean.title = size;
                        bean.selfFlag = true;
                        getItem.add(bean);*//*
                        //mAddItemListener.onAddItemListener(size,getItem.size());
                        //notifyItemChanged(getItemCount());
                    } else {
                        UIUtil.toastShort(mContext, "请输入规格参数");
                    }
                }
                return false;
            }
        });*/


    }


    @Override
    public int getItemCount() {
        try {
            if (getItem != null && getItem.size() > 0) {
                return getItem./*get(mPosition).*/size() + 1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
/*
    private class AttOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
            } else {
                if(v instanceof  EditText &&"".equals((((EditText) v).getText()).toString()))

                    if(v.getTag()!=null&&(v.getTag()instanceof Integer)&&(int)v.getTag()!=getItemCount())
                        mItemDeleteListener.onReduceItemListener((int)v.getTag());


                //notifyDataSetChanged();
            }
        }
    }
*/


    private class AttTextWatcher implements TextWatcher{

        GoodsSpecTypeViewHolder holder = null;
        int lastPosition;
        String lastString;
        public AttTextWatcher(GoodsSpecTypeViewHolder holder){
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
/*            if(s==null||"".equals(s.toString().trim())||!holder.mTv_size.hasFocus()) {
                return;
            }*/
            if(holder.mTv_size==null||holder.mTv_size.getTag()==null) {
                return;
            }else {
                if(!(holder.mTv_size.getTag()instanceof Integer)) {
                    return;
                }
            }
            int position = (Integer) holder.mTv_size.getTag();
            if(s.toString().trim().length()>0)
                mAddItemListener.onAddItemListener(s.toString().trim(),position);
            else
                mItemDeleteListener.onReduceItemListener(position);
        }

    }


    class GoodsSpecTypeViewHolder extends RecyclerView.ViewHolder {


        //private final EditText mEt;
        //private final RelativeLayout mRl;

        private final EditText mTv_size;
        private final RelativeLayout mRl_del;
        private final ImageView mIv_del;

        AttTextWatcher attTextWatcher;

        public GoodsSpecTypeViewHolder(View itemView) {
            super(itemView);
            //mEt = itemView.findViewById(R.id.et_spec_self);
            //mRl = itemView.findViewById(R.id.rl_content);
            mTv_size = itemView.findViewById(R.id.tv_size);
            mRl_del = itemView.findViewById(R.id.rl_del);
            mIv_del = itemView.findViewById(R.id.iv_del);
            attTextWatcher = new AttTextWatcher(this);
            mTv_size.addTextChangedListener(attTextWatcher);
            //mTv_size.setOnFocusChangeListener(new AttOnFocusChangeListener());
        }
    }

    private ImageRecyclerReduceItemListener mItemDeleteListener;
    private RecyclerViewAddOneListener mAddItemListener;

    public void setAddItem(RecyclerViewAddOneListener addItemListener) {
        mAddItemListener = addItemListener;
    }
    public void setOnItemDeleteListener(ImageRecyclerReduceItemListener itemDeleteListener) {
        mItemDeleteListener = itemDeleteListener;
    }

}
