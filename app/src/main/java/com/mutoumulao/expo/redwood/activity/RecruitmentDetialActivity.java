package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.HomeImageAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/26.
 */

public class RecruitmentDetialActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.rl_right)
    RelativeLayout mRlRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.tv_article_name)
    TextView mTvArticleName;
    @BindView(R.id.iv_author_head)
    CircleImageView mIvAuthorHead;
    @BindView(R.id.tv_author_name)
    TextView mTvAuthorName;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.rv_photo)
    BaseRecyclerView mRvPhoto;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.btn_go)
    Button mBtnGo;
    private String mPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_detial);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTitle.setText("详情");
        mBtnGo.setText("电话联系");
        PositionEntity entity = (PositionEntity) getIntent().getSerializableExtra("entity");
        if (entity != null) {
            mTvContent.setText(Html.fromHtml(entity.content));
            if (!TextUtils.isEmpty(entity.images)) {
                final List<String> list = JSON.parseArray(entity.images, String.class);
                if (list.size() > 0) {
                    HomeImageAdapter imageAdapter = new HomeImageAdapter(this, list);
                    imageAdapter.setLayoutType(1);
                    mRvPhoto.setAdapter(imageAdapter);
                    mRvPhoto.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
                        @Override
                        public void onItemClick(RecyclerView parent, View view, int position, long id) {
                            List<LocalMedia> medias = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                LocalMedia localMedia = new LocalMedia();
                                localMedia.setPath(UrlConst.IMAGE_URL + list.get(i));
                                medias.add(localMedia);
//                        PictureSelector.create(mBaseActivity).externalPicturePreview(position, medias);
                            }
                            PictureSelector.create(RecruitmentDetialActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, medias);

                        }
                    });
                    mRvPhoto.setNestedScrollingEnabled(false);
                }
            }

            mTvArticleName.setText(entity.title);
            new BitmapTools(this).display(mIvAuthorHead, UrlConst.IMAGE_URL + entity.avatar);
            mTvTime.setText(entity.time);
            mTvAuthorName.setText(entity.nickname);

            mPhone = entity.phone;

        }

    }

    @OnClick({R.id.rl_back, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_go:
                if (TextUtils.isEmpty(mPhone)) {
                    UIUtil.toastShort(this, "未留电话");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mPhone);
                intent.setData(data);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
