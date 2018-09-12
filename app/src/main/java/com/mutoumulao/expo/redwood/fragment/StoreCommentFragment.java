package com.mutoumulao.expo.redwood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.StoreCommentAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;
import com.mutoumulao.expo.redwood.entity.StoreCommentEntity;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/29.
 */

public class StoreCommentFragment extends BaseFragment {

    private PullToRefreshView mPull_view;
    private BaseRecyclerView mRv_message;
    private String mStore_id;
    private int pages = 1;
    private List<StoreCommentEntity> mList = new ArrayList<>();
    private StoreCommentAdapter mAdapter;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);
    }

    @Override
    protected void initViews() {
        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPull_view = (PullToRefreshView) findViewById(R.id.pull_view);
        mAdapter = new StoreCommentAdapter(mBaseActivity, mList);
        mAdapter.setFragment(this);
        mRv_message.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        GoodsDetialEntity entity = (GoodsDetialEntity) arguments.getSerializable("storeManagerListEntity");
        mStore_id = entity.id;

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
        MineFuncManager.getInstance().getCommentList(page,mStore_id,new FinalResponseCallback<StoreCommentEntity>(mPull_view){
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(mBaseActivity);
                }
            }

            @Override
            public void onSuccess(List<StoreCommentEntity> t) {
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
