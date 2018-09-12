package com.mutoumulao.expo.redwood.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.StoreManagerListAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
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

/**
 * Created by lzy on 2018/8/27.
 */

public class MyBrowseHistory extends BaseActivity {

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
    @BindView(R.id.recycler_view)
    BaseRecyclerView mRecyclerView;
    @BindView(R.id.pull_view)
    PullToRefreshView mPullView;

    private int pages = 1;
    private List<StoreManagerListEntity> mList = new ArrayList<>();
    private StoreManagerListAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browse_history);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTitle.setText("我的足迹");
        mAdapter = new StoreManagerListAdapter(this, mList);
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
    }

    private void loadData(final int page) {
        MineFuncManager.getInstance().getBrowseHistory(page, new FinalResponseCallback<StoreManagerListEntity>(mPullView) {
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(MyBrowseHistory.this);
                }
            }

            @Override
            public void onSuccess(List<StoreManagerListEntity> t) {
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
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if(t.size()<10){
                    mPullView.setLoadMoreEnable(false);
                }
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
                UIUtil.hideProgressBar(MyBrowseHistory.this);
                if (1 == page) {
                    mPullView.onHeaderRefreshComplete();
                } else {
                    mPullView.onFooterRefreshComplete();
                }
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
