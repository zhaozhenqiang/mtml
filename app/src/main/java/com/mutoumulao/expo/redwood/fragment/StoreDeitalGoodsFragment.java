package com.mutoumulao.expo.redwood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.StoreManagerListAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/14.
 */

public class StoreDeitalGoodsFragment extends BaseFragment {
    private BaseRecyclerView mRv_message;
    private int pages = 1;
    private List<StoreManagerListEntity> mList = new ArrayList<>();

    private PullToRefreshView mPull_view;
    private StoreManagerListAdapter mAdapter;
    private String mStore_id;
    private String mUid;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);
    }

    @Override
    protected void initViews() {
        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPull_view = (PullToRefreshView) findViewById(R.id.pull_view);
        mAdapter = new StoreManagerListAdapter(mBaseActivity, mList);
        mRv_message.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mStore_id = arguments.getString("store_id");
        mUid = arguments.getString("uid");
        loadData(1);

        // 监听上拉下拉
        mPull_view.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1);
            }
        });

        mPull_view.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages);
            }
        });
    }


    private void loadData(final int page) {
        MineFuncManager.getInstance().getGoodsList(page,mUid, new FinalResponseCallback<StoreManagerListEntity>(mPull_view) {
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(mBaseActivity);
                }
            }

            @Override
            public void onSuccess(List<StoreManagerListEntity> t) {
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

                if (t.size() < 10) {
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
                loadData(1);
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
