package com.mutoumulao.expo.redwood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.ArticleWebViewActivity;
import com.mutoumulao.expo.redwood.adapter.HomesMessageAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.HomeListEntity;
import com.mutoumulao.expo.redwood.logic.HomeManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/9.
 * 首页资讯fragment
 */

public class HomesMessageFragment extends BaseFragment {

    private BaseRecyclerView mRv_message;
    private int pages = 1;
    private List<HomeListEntity> mList = new ArrayList<>();
    private HomesMessageAdapter mAdapter;
    private PullToRefreshView mPull_view;
    private String category = "";

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);
    }

    @Override
    protected void initViews() {
        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPull_view = (PullToRefreshView) findViewById(R.id.pull_view);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        final Bundle mBundle = getArguments();
        String position = mBundle.getString("arg");

        if ("0".equals(position)) {
            category = "推荐";
        } else if ("1".equals(position)) {
            category = "热门";
        } else if ("2".equals(position)) {
            category = "最新";
        } else {
            category = "推荐";
        }
        mAdapter = new HomesMessageAdapter(mBaseActivity, mList);
        mRv_message.setAdapter(mAdapter);
        loadData(1, category);
        mPull_view.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1, category);
            }
        });

        mPull_view.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages, category);
            }
        });

        mRv_message.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                final HomeListEntity item = mList.get(position);
                /*Intent intent = new Intent(mBaseActivity, ArticleDetialActivity.class);
                intent.putExtra(EXTECL_ARTICLE,item);
                startActivity(intent);*/
                Intent intent = new Intent(mBaseActivity, ArticleWebViewActivity.class);
                intent.putExtra("id",item.id);
                startActivity(intent);
            }
        });
    }

    private void loadData(final int page, final String category) {
        HomeManager.getInstance().getArticleList(page, category, new FinalResponseCallback<HomeListEntity>(mPull_view) {
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(mBaseActivity);
                }
            }

            @Override
            public void onSuccess(List<HomeListEntity> t) {
                super.onSuccess(t);
                mPull_view.onHeaderRefreshComplete();
                if (1 == page) {
                    mList.clear();
                    if (0 == t.size()) {
                        setIsEmpty(true);
                    } else {
                        mList.addAll(t);
                    }
                } else {
                    mList.addAll(t);
                    mPull_view.setLoadMoreEnable(true);
                    mPull_view.onFooterRefreshComplete();
                }
                pages++;
//                mRv_message.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if(t.size()<10){
                    mPull_view.setLoadMoreEnable(false);
                }

            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onReload() {
                super.onReload();
                loadData(1, category);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(mBaseActivity);
                if (1 == page) {
                    mPull_view.onHeaderRefreshComplete();
                } else {
                    mPull_view.onFooterRefreshComplete();
                }
            }
        });
    }
}
