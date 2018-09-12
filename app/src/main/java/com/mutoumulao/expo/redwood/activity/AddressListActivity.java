package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.AddressListAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.AddressEntity;
import com.mutoumulao.expo.redwood.entity.event.AdressEvent;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/19.
 */

public class AddressListActivity extends BaseActivity {
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
    @BindView(R.id.rv_address)
    BaseRecyclerView mRvAddress;
    @BindView(R.id.btn_go)
    Button mBtnGo;

    private List<AddressEntity> mList = new ArrayList<>();
    private AddressListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mTvTitle.setText("收货地址");
        mBtnGo.setText("添加新地址");
        mAdapter = new AddressListAdapter(this, mList);
        mRvAddress.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("address_id", mList.get(position).id);
                intent.putExtra("user_name", mList.get(position).name);
                intent.putExtra("address", mList.get(position).address);
                intent.putExtra("phone", mList.get(position).phone);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        loadData();
    }

    private void loadData() {
        ShoppingManager.getInstance().addressList(new FinalResponseCallback<AddressEntity>(this){
            @Override
            public void onSuccess(List<AddressEntity> t) {
                super.onSuccess(t);
                mList.clear();
                mList.addAll(t);
                mRvAddress.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(AddressListActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(AddressListActivity.this);
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
                startActivity(new Intent(this, CreateAddressActivity.class));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(AdressEvent event){
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
