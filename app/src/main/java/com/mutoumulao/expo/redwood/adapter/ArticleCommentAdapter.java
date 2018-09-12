package com.mutoumulao.expo.redwood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseAdapter;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.HomeListEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;

import java.util.List;

/**
 * Created by lzy on 2018/8/10.
 */

public class ArticleCommentAdapter extends BaseAdapter<HomeListEntity.CommentEntity,ArticleCommentAdapter.ArticleViewHolder> {

    private final BitmapTools mBitmapTools;

    public ArticleCommentAdapter(Context context, List<HomeListEntity.CommentEntity> list) {
        super(context, list);
        mBitmapTools = new BitmapTools(context);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_article_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        HomeListEntity.CommentEntity item = getItem(position);
        holder.mTv_author_name.setText(item.nickname);
        holder.mTv_content.setText(Html.fromHtml(item.content));
        mBitmapTools.display(holder.mIv_author_head, UrlConst.IMAGE_URL + item.avatar);
        holder.mTv_time.setText(item.time);

    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mIv_author_head;
        private final TextView mTv_author_name;
        private final TextView mTv_time;
        private final TextView mTv_content;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            mIv_author_head = itemView.findViewById(R.id.iv_author_head);
            mTv_author_name = itemView.findViewById(R.id.tv_author_name);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
