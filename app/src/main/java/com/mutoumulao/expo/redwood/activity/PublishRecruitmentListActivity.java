package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.FuncListAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;
import com.mutoumulao.expo.redwood.entity.event.PublishRecruitmentEntity;
import com.mutoumulao.expo.redwood.fragment.MineFragment;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;
import com.mutoumulao.expo.redwood.view.PullToRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/3.
 */

public class PublishRecruitmentListActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    BaseRecyclerView mRecyclerView;
    @BindView(R.id.pull_view)
    PullToRefreshView mPullView;
    @BindView(R.id.btn_go)
    Button mBtnGo;
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rl_title)
    LinearLayout mRlTitle;

    private int pages = 1;
    private List<PositionEntity> mList = new ArrayList<>();
    private FuncListAdapter mAdapter;
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_recruitment_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

    }

    private void loadData(final int page, final String type) {
        String keyword = mEtSearch.getText().toString().trim();
        if ("0".equals(type)) { //服务
            MineFuncManager.getInstance().getServiceList(page, keyword, new FinalResponseCallback<PositionEntity>(mPullView) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(PublishRecruitmentListActivity.this);
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
                    loadData(1, type);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(PublishRecruitmentListActivity.this);
                    if (1 == page) {
                        mPullView.onHeaderRefreshComplete();
                    } else {
                        mPullView.onFooterRefreshComplete();
                    }
                }
            });
        } else if ("1".equals(type)) {//招聘管理
            MineFuncManager.getInstance().getRecruitmentList(page,keyword , new FinalResponseCallback<PositionEntity>(mPullView) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(PublishRecruitmentListActivity.this);
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
                    loadData(1, type);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(PublishRecruitmentListActivity.this);
                    if (1 == page) {
                        mPullView.onHeaderRefreshComplete();
                    } else {
                        mPullView.onFooterRefreshComplete();
                    }
                }
            });
        }else if("2".equals(mType)){//求职
            MineFuncManager.getInstance().getJobList(page,keyword , new FinalResponseCallback<PositionEntity>(mPullView) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (1 == page) {
                        UIUtil.showProgressBar(PublishRecruitmentListActivity.this);
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
                    if (t.size() <10) {
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
                    loadData(1, type);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(PublishRecruitmentListActivity.this);
                    if (1 == page) {
                        mPullView.onHeaderRefreshComplete();
                    } else {
                        mPullView.onFooterRefreshComplete();
                    }
                }
            });
        }

    }

    private void initView() {
        /*String name = getIntent().getStringExtra(MineFragment.EXTECL_NAME);
        if (name != null) {
            mTvTitle.setText(name);
        }*/
        mBtnGo.setText("发布内容");

        mType = getIntent().getStringExtra(MineFragment.EXTECL_TYPE);

        loadData(1, mType);

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        loadData(1,mType);
                    }else {
                        UIUtil.toastShort(PublishRecruitmentListActivity.this,"请输入关键字");
                    }
                }
                return false;
            }
        });


        // 监听上拉下拉
        mPullView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                pages = 1;
                loadData(1, mType);
            }
        });

        mPullView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                loadData(pages, mType);
            }
        });

        mAdapter = new FuncListAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnEditListener(new ImageRecyclerReduceItemListener() {
            @Override
            public void onReduceItemListener(int position) {
                toEdit(position);
            }
        });

        mAdapter.setOnDeleteListerer(new ImageRecyclerReduceItemListener() {
            @Override
            public void onReduceItemListener(int position) {
                delete(position);
            }
        });
    }

    private void delete(final int position) {
        PositionEntity entity = mList.get(position);
        if ("0".equals(mType)) { //服务
            MineFuncManager.getInstance().deleteService(entity.id, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(PublishRecruitmentListActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(PublishRecruitmentListActivity.this);
                }
            });
        } else if ("1".equals(mType)) {//招聘管理
            MineFuncManager.getInstance().deleteRecruitment(entity.id, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(PublishRecruitmentListActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(PublishRecruitmentListActivity.this);
                }
            });
        } else if("2".equals(mType)){//求职
            MineFuncManager.getInstance().deleteJob(entity.id, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onStart() {
                    super.onStart();
                    UIUtil.showProgressBar(PublishRecruitmentListActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    UIUtil.hideProgressBar(PublishRecruitmentListActivity.this);
                }
            });
        }
    }

    private void toEdit(int position) {//0 服务 ;1招聘管理
        Intent intent = new Intent(this, EditRecruitmentActivity.class);
        intent.putExtra(MineFragment.EXTECL_TYPE, mType);
        intent.putExtra("entity", mList.get(position));
        startActivity(intent);

    }

    @OnClick({R.id.rl_back, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.btn_go:
                Intent intent = new Intent(this, PublishRecruitmentActivity.class);
                intent.putExtra(MineFragment.EXTECL_TYPE, mType);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(PublishRecruitmentEntity entity) {
        loadData(1, entity.type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
