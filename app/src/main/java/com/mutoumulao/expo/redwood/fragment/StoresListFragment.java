package com.mutoumulao.expo.redwood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.StoreDetialActivity;
import com.mutoumulao.expo.redwood.adapter.StoreListAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
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

public class StoresListFragment extends BaseFragment {
    private BaseRecyclerView mRv_message;
    private int pages = 1;
    private List<StoreEntity> mList = new ArrayList<>();

    private PullToRefreshView mPull_view;
    private StoreListAdapter mAdapter;
    private String mType;
    private String mCategory;
    private int sort = 2;
    private String key_word= "";


    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPull_view = (PullToRefreshView) findViewById(R.id.pull_view);
        mAdapter = new StoreListAdapter(mBaseActivity, mList);
        mRv_message.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();;
        mCategory = bundle.getString("category");
        mType = bundle.getString("type");
        sort = bundle.getInt("sort",2);

        loadData(1, mType);

        // 监听上拉下拉
        mPull_view.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1 , mType);
            }
        });

        mPull_view.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages , mType);
            }
        });

        mRv_message.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                StoreEntity storeEntity = mList.get(position);
                Intent intent = new Intent(mBaseActivity, StoreDetialActivity.class);
                intent.putExtra("storeEntity",storeEntity);
                startActivity(intent);
            }
        });
    }


    private void loadData(final int page,final String type) {
        MineFuncManager.getInstance().getStoreList(page, key_word,type, sort + "",new FinalResponseCallback<StoreEntity>(mPull_view) {
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(mBaseActivity);
                }
            }

            @Override
            public void onSuccess(List<StoreEntity> t) {
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
                loadData(1, type);
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
    public void onEvent(HomeSearchEvent event){
        key_word = event.key_word;
        loadData(1 , mType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
