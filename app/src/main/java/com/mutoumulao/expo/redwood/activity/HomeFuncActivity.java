package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.HomeFuncListAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mutoumulao.expo.redwood.fragment.HomeFragment.EXTRCLE_TITLE;

/**
 * Created by lzy on 2018/8/13.
 * 首页 服务 招聘，求职
 */

public class HomeFuncActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rl_title)
    LinearLayout mRlTitle;
    @BindView(R.id.rv_func)
    BaseRecyclerView mRvFunc;
    @BindView(R.id.pull_view)
    PullToRefreshView mPullView;

    private String mTitle;
    private int pages = 1;
    private List<PositionEntity> mList = new ArrayList<>();
    private HomeFuncListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_func);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTitle = getIntent().getStringExtra(EXTRCLE_TITLE);
        loadData(1);

        // 监听上拉下拉
        mPullView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1);
            }
        });

        mPullView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages);
            }
        });

        mAdapter = new HomeFuncListAdapter(this, mList);
        mRvFunc.setAdapter(mAdapter);

        mRvFunc.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                PositionEntity entity = mList.get(position);
                Intent intent = new Intent(HomeFuncActivity.this,RecruitmentDetialActivity.class);
                if ("服务".equals(mTitle)) {

                } else if ("招聘".equals(mTitle)) {

                } else if ("求职".equals(mTitle)) {

                }
                intent.putExtra("entity",entity);
                startActivity(intent);
            }
        });

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        loadData(1);
                    }else {
                        UIUtil.toastShort(HomeFuncActivity.this,"请输入关键字");
                    }
                }
                return false;
            }
        });
    }

    private void loadData(final int page) {
        String keyword = mEtSearch.getText().toString().trim();
        if ("服务".equals(mTitle)) {

            MineFuncManager.getInstance().getServiceList(page, keyword, new FinalResponseCallback<PositionEntity>(this) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(HomeFuncActivity.this);
                    }
                }

                @Override
                public void onSuccess(List<PositionEntity> t) {
                    super.onSuccess(t);
                    mPullView.onHeaderRefreshComplete();
                    if (1 == page) {
                        mList.clear();
                        if (0 == t.size()) {
                            setIsEmpty(true);
                        } else {
                            mList.addAll(t);
                        }

                    } else {
                        mList.addAll(t);
                        mPullView.setLoadMoreEnable(true);
                        mPullView.onFooterRefreshComplete();
                    }
                    if(t.size()<10){
                        mPullView.setLoadMoreEnable(false);
                    }
                    pages++;
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public boolean isShowNotice() {
                    return false;
                }

                @Override
                public void onReload() {
                    super.onReload();
                    loadData(1);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(HomeFuncActivity.this);
                    if (1 == page) {
                        mPullView.onHeaderRefreshComplete();
                    } else {
                        mPullView.onFooterRefreshComplete();
                    }
                }
            });

        } else if ("招聘".equals(mTitle)) {

            MineFuncManager.getInstance().getRecruitmentList2(page,keyword, new FinalResponseCallback<PositionEntity>(this) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(HomeFuncActivity.this);
                    }
                }

                @Override
                public void onSuccess(List<PositionEntity> t) {
                    super.onSuccess(t);
                    mPullView.onHeaderRefreshComplete();
                    if (1 == page) {
                        mList.clear();
                        if (0 == t.size()) {
                            setIsEmpty(true);
                        } else {
                            mList.addAll(t);
                        }
                    } else {
                        mList.addAll(t);
                        mPullView.setLoadMoreEnable(true);
                        mPullView.onFooterRefreshComplete();
                    }
                    pages++;
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public boolean isShowNotice() {
                    return false;
                }

                @Override
                public void onReload() {
                    super.onReload();
                    loadData(1);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(HomeFuncActivity.this);
                    if (1 == page) {
                        mPullView.onHeaderRefreshComplete();
                    } else {
                        mPullView.onFooterRefreshComplete();
                    }
                }
            });

        } else if ("求职".equals(mTitle)) {

            MineFuncManager.getInstance().getJobList1(page, keyword, new FinalResponseCallback<PositionEntity>(this) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(HomeFuncActivity.this);
                    }
                }

                @Override
                public void onSuccess(List<PositionEntity> t) {
                    super.onSuccess(t);
                    mPullView.onHeaderRefreshComplete();
                    if (1 == page) {
                        mList.clear();
                        if (0 == t.size()) {
                            setIsEmpty(true);
                        } else {
                            mList.addAll(t);
                        }
                    } else {
                        mList.addAll(t);
                        mPullView.setLoadMoreEnable(true);
                        mPullView.onFooterRefreshComplete();
                    }
                    pages++;
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public boolean isShowNotice() {
                    return false;
                }

                @Override
                public void onReload() {
                    super.onReload();
                    loadData(1);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(HomeFuncActivity.this);
                    if (1 == page) {
                        mPullView.onHeaderRefreshComplete();
                    } else {
                        mPullView.onFooterRefreshComplete();
                    }
                }
            });

        }
    }

    @OnClick(R.id.rl_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
