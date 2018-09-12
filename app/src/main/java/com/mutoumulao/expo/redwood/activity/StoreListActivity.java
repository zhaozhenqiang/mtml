package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.StoreManagerListAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.event.CreateGoodsEventy;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
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

public class StoreListActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_store)
    BaseRecyclerView mRvStore;
    @BindView(R.id.btn_go)
    Button mBtnGo;
    @BindView(R.id.pull_view)
    PullToRefreshView mPullView;

    private int pages = 1;
    private int positions = 0;
    private List<StoreManagerListEntity> mList = new ArrayList<>();

    private StoreManagerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        ButterKnife.bind(this);
        initView();
        loadData(1);
        EventBus.getDefault().register(this);
    }

    private void loadData(final int page) {
        MineFuncManager.getInstance().getGoodsList(page, new FinalResponseCallback<StoreManagerListEntity>(mPullView) {
            @Override
            public void onStart() {
                super.onStart();
                if (1 == page) {
                    UIUtil.showProgressBar(StoreListActivity.this);
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
                mAdapter.notifyDataSetChanged();
                if (t.size() < 10) {
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
                UIUtil.hideProgressBar(StoreListActivity.this);
                if (1 == page) {
                    mPullView.onHeaderRefreshComplete();
                } else {
                    mPullView.onFooterRefreshComplete();
                }
            }
        });
    }

    private void initView() {
        mTvTitle.setText("商品管理");
        mBtnGo.setText("发布商品");
        mAdapter = new StoreManagerListAdapter(this, mList);
        mRvStore.setAdapter(mAdapter);
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
        mRvStore.setOnItemLongClickListener(new BaseRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
                shoPopWindow(position);
                return false;
            }
        });


    }

    private void shoPopWindow(final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.dialog_style).create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.BOTTOM;
        params.width = DisplayUtil.getScreenWidth(this);
        window.setAttributes(params);
        window.setContentView(R.layout.dialog_edit_delete);

        LinearLayout ll_edit = window.findViewById(R.id.ll_edit);

        LinearLayout ll_delete = window.findViewById(R.id.ll_delete);


        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(position);  //删除事件的方法
                dialog.dismiss();
            }
        });

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreManagerListEntity entity = mList.get(position);
                Intent intent = new Intent(StoreListActivity.this, PublishCommodityActivity.class);
                intent.putExtra("store_entity", entity);
                intent.putExtra("is_edit", true);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        ;
    }


    private void deleteData(final int position) {
        StoreManagerListEntity entity = mList.get(position);
        MineFuncManager.getInstance().goodsDel(entity.id, new ResponseCallback() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(StoreListActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(StoreListActivity.this);
            }
        });
    }

    @OnClick({R.id.rl_back, R.id.btn_go})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_go:
                startActivity(new Intent(this, PublishCommodityActivity.class));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(CreateGoodsEventy eventy) {
        loadData(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
