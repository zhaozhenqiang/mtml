package com.mutoumulao.expo.redwood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.StoreManagerListAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.event.HomeSearchEvent;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/14.
 */

public class MeterialsListFragment extends BaseFragment {
    private BaseRecyclerView mRv_message;
    private int pages = 1;
    private List<StoreManagerListEntity> mList = new ArrayList<>();

    private PullToRefreshView mPull_view;
    private StoreManagerListAdapter mAdapter;
    private String mType;
    private String mCategory;
    private int sort = 2;
    private String author_id = "";
    private String key_word = "";

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPull_view = (PullToRefreshView) findViewById(R.id.pull_view);
        mAdapter = new StoreManagerListAdapter(mBaseActivity, mList);
        mRv_message.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        ;
        mCategory = bundle.getString("category");
        mType = bundle.getString("type");
        sort = bundle.getInt("sort", 2);
        if ("家具".equals(mCategory)) {
            author_id = "";
        } else if ("原材".equals(mCategory)) {
            author_id = ""/*SharedPutils.getPreferences(mBaseActivity).getUserID()*/;
        } else if ("厂家".equals(mCategory)) {

        }
        loadData(1, author_id, mCategory, key_word, mType);

        // 监听上拉下拉
        mPull_view.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1, author_id, mCategory, key_word, mType);
            }
        });

        mPull_view.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages, author_id, mCategory, key_word, mType);
            }
        });
    }


    private void loadData(final int page, final String author_id, final String category, final String key_word, final String type) {
        MineFuncManager.getInstance().getGoodsList(page, author_id, category, type, sort + "", key_word, new FinalResponseCallback<StoreManagerListEntity>(mPull_view) {
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
                loadData(1, author_id, category, type, key_word);
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

    @Subscribe
    public void onEvent(HomeSearchEvent event) {
        key_word = event.key_word;
        loadData(1, author_id, mCategory, key_word, mType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
