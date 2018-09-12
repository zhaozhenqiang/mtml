package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.ArticleWebViewActivity;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.entity.HomeListEntity;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.util.List;

/**
 * Created by lzy on 2018/8/10.
 */

public class HomesMessageAdapter extends BaseAdapter<HomeListEntity,HomesMessageAdapter.HomesMessageViewHolder> {

    public HomesMessageAdapter(Context context, List<HomeListEntity> list) {
        super(context, list);
    }

    @Override
    public HomesMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomesMessageViewHolder(mInflater.inflate(R.layout.item_home_news, null));
    }

    @Override
    public void onBindViewHolder(HomesMessageViewHolder holder, int position) {
        final HomeListEntity item = getItem(position);
        holder.mTv_title_name.setText(item.title);
        holder.mTv_time.setText(item.time);
        if (!TextUtils.isEmpty(item.images)) {
            List<String> list = JSON.parseArray(item.images, String.class);
            HomeImageAdapter adapter = new HomeImageAdapter(mContext, list);
            holder.mRv_photo.setAdapter(adapter);
        }
        holder.mRv_photo.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ArticleWebViewActivity.class);
                intent.putExtra("id",item.id);
                mContext.startActivity(intent);
               /* Intent intent = new Intent(mContext, ArticleDetialActivity.class);
                intent.putExtra(EXTECL_ARTICLE,item);
                mContext.startActivity(intent);*/
            }
        });
       /* holder.mLl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleDetialActivity.class);
                intent.putExtra(EXTECL_ARTICLE,item);
                mContext.startActivity(intent);
            }
        });*/
    }

    class HomesMessageViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTv_title_name;
        private final TextView mTv_time;
        private final BaseRecyclerView mRv_photo;
        private final LinearLayout mLl_content;

        public HomesMessageViewHolder(View itemView) {
            super(itemView);
            mTv_title_name = itemView.findViewById(R.id.tv_title_name);
            mLl_content = itemView.findViewById(R.id.ll_content);
            mRv_photo = itemView.findViewById(R.id.rv_photo);
            mTv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
