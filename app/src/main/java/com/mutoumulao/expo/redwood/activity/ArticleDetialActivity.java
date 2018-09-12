package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.ArticleCommentAdapter;
import com.mutoumulao.expo.redwood.adapter.HomeImageAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.HomeListEntity;
import com.mutoumulao.expo.redwood.logic.HomeManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/10.
 * 文章详情
 */

public class ArticleDetialActivity extends BaseActivity {

    public static String EXTECL_ARTICLE = "article";
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_photo)
    BaseRecyclerView mRvPhoto;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_comment_num)
    TextView mTvCommentNum;
    @BindView(R.id.rv_comment)
    BaseRecyclerView mRvComment;
    @BindView(R.id.tv_article_name)
    TextView mTvArticleName;
    @BindView(R.id.iv_author_head)
    CircleImageView mIvAuthorHead;
    @BindView(R.id.tv_author_name)
    TextView mTvAuthorName;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.et_message)
    EditText mEtMessage;

    private List<HomeListEntity.CommentEntity> mList = new ArrayList<>();
    private SharedPutils mPutils;
    private ArticleCommentAdapter mCommentAdapter;
    private List<String> mList_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detial);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mList.clear();
        mPutils = SharedPutils.getPreferences(this);
        BitmapTools bitmapTools = new BitmapTools(this);
        final HomeListEntity article = (HomeListEntity) getIntent().getSerializableExtra(EXTECL_ARTICLE);
        if (!TextUtils.isEmpty(article.images)) {

            mList_photo = JSON.parseArray(article.images, String.class);

            HomeImageAdapter imageAdapter = new HomeImageAdapter(this, mList_photo);
            mRvPhoto.setAdapter(imageAdapter);

            mRvPhoto.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, long id) {
                    List<LocalMedia> medias = new ArrayList<>();
                    for (int i = 0; i < mList_photo.size(); i++) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(UrlConst.IMAGE_URL + mList_photo.get(i));
                        medias.add(localMedia);
//                        PictureSelector.create(mBaseActivity).externalPicturePreview(position, medias);
                    }
                    PictureSelector.create(ArticleDetialActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, medias);
                }
            });

        }
        final String id = article.id;
        mList.addAll(article.list);
        mCommentAdapter = new ArticleCommentAdapter(this, mList);
        mRvComment.setAdapter(mCommentAdapter);

        mTvArticleName.setText(article.title);
        bitmapTools.display(mIvAuthorHead, UrlConst.IMAGE_URL + article.avatar);
        mTvTime.setText(article.time);
        mTvAuthorName.setText(article.author_name);
        mTvCommentNum.setText("评论" + article.list.size() + "条");
        mTvContent.setText(Html.fromHtml(article.content));
        mTvTitle.setText("详情");

        mEtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String content = mEtMessage.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        sendMessage(id, content);
                    } else {
                        UIUtil.toastShort(ArticleDetialActivity.this, "请输入评论内容");
                    }
                }
                return false;
            }
        });
    }

    private void sendMessage(final String id, final String content) {
        HomeManager.getInstance().SendComment(id, content, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mEtMessage.setText("");
                HomeListEntity.CommentEntity entity = new HomeListEntity.CommentEntity();
                entity.article_id = id;
                entity.avatar = mPutils.getAvatar();
                entity.author_id = mPutils.getUserID();
                entity.content = content;
                entity.time = "刚刚";
                entity.nickname = mPutils.getUserName();
                mList.add(entity);
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(ArticleDetialActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(ArticleDetialActivity.this);
            }
        });
    }

    @OnClick(R.id.rl_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            default:
                break;
        }
    }
}
