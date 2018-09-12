package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.fragment.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/23.
 */

public class StoreManagerActivity extends BaseActivity {

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
    @BindView(R.id.ll_order)
    LinearLayout mLlOrder;
    @BindView(R.id.ll_store)
    LinearLayout mLlStore;
    @BindView(R.id.ll_merchant)
    LinearLayout mLlMerchant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store_manager);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String name = getIntent().getStringExtra(MineFragment.EXTECL_NAME);
        mTvTitle.setText(name);
        Window window = getWindow();
        //如果系统5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.title));
        }
    }

    @OnClick({R.id.rl_back, R.id.ll_order, R.id.ll_store, R.id.ll_merchant})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_order:
                Intent intent = new Intent(this, OrderListActivity.class);
                intent.putExtra("position_order", 0);
                intent.putExtra("from", "store");
                startActivity(intent);
                break;
            case R.id.ll_store:
                startActivity(new Intent(this, StoreListActivity.class));
                break;
            case R.id.ll_merchant:
                startActivity(new Intent(this, CreateStoreActivity.class).putExtra("is_edit", true));
                break;
            default:
                break;
        }
    }
}
