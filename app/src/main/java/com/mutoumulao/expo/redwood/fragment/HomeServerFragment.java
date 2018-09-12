package com.mutoumulao.expo.redwood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.RecruitmentDetialActivity;
import com.mutoumulao.expo.redwood.adapter.HomeFuncListAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.entity.event.HomeServiceSearchEvent;
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
 * Created by lzy on 2018/8/30.
 */

public class HomeServerFragment extends BaseFragment {

    private BaseRecyclerView mRv_message;
    private int pages = 1;
    private List<PositionEntity> mList = new ArrayList<>();
    private HomeFuncListAdapter mAdapter;
    private PullToRefreshView mPullView;
    private String keyword;
    private String category_name;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_home, null);

    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mRv_message = (BaseRecyclerView) findViewById(R.id.rv_message);
        mPullView = (PullToRefreshView) findViewById(R.id.pull_view);
        mAdapter = new HomeFuncListAdapter(mBaseActivity,mList);
        mRv_message.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        String position = mBundle.getString("arg");
        if("0".equals(position)){
            category_name = "";
        }else if("1".equals(position)){
            category_name = "运输";

        }else if("2".equals(position)){
            category_name = "修理";

        }else if("3".equals(position)){
            category_name = "打包";

        }else if("4".equals(position)){
            category_name = "搬运";

        }else if("5".equals(position)){
            category_name = "配件";

        }else if("6".equals(position)){
            category_name = "物流";

        }else if("7".equals(position)){
            category_name = "油漆";

        }else if("8".equals(position)){
            category_name = "机器";
        }
        loadData(1);
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

        mRv_message.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                PositionEntity entity = mList.get(position);
                Intent intent = new Intent(mBaseActivity,RecruitmentDetialActivity.class);
                intent.putExtra("entity",entity);
                startActivity(intent);
            }
        });
    }

    private void loadData(final int page) {
        MineFuncManager.getInstance().getServiceList(page, keyword,category_name, new FinalResponseCallback<PositionEntity>(this) {
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(mBaseActivity);
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
//                mRv_message.setAdapter(mAdapter);
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
                UIUtil.hideProgressBar(mBaseActivity);
                if (1 == page) {
                    mPullView.onHeaderRefreshComplete();
                } else {
                    mPullView.onFooterRefreshComplete();
                }
            }
        });
    }

    @Subscribe
    public void onEvent(HomeServiceSearchEvent event) {
        keyword = event.key_word;
        loadData(1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
