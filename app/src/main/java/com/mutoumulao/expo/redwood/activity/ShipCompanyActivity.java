package com.mutoumulao.expo.redwood.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.ShipCompanyAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.ShipCompanyEntity;
import com.mutoumulao.expo.redwood.entity.event.OrderEvent;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mutoumulao.expo.redwood.util.UIUtil.showAlert;

/**
 * Created by lzy on 2018/8/30.
 */

public class ShipCompanyActivity extends BaseActivity {
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
    @BindView(R.id.rv_ship_company)
    BaseRecyclerView mRvShipCompany;

    private List<ShipCompanyEntity> mList = new ArrayList<>();
    private String mOrder_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_company);
        ButterKnife.bind(this);
        mOrder_id = getIntent().getStringExtra("order_id");
        mTvTitle.setText("选择物流公司");
        loadData();
        mRvShipCompany.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                showWindows(position);
            }
        });
    }

    private void showWindows(final int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_confirm_edit, null);
        final Dialog dialog = showAlert(this, view);
        TextView titleText = (TextView) view.findViewById(R.id.tv_confirm_title);
        final EditText msgText = (EditText) view.findViewById(R.id.et_confirm_msg);
        Button leftBtn = (Button) view.findViewById(R.id.btn_confirm_left);
        Button rightBtn = (Button) view.findViewById(R.id.btn_confirm_right);
        titleText.setText("请输入订单号");
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String msg = msgText.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    UIUtil.toastShort(ShipCompanyActivity.this, "请输入单号");
                } else {
                    sendOrder(msg,position);

                }
            }
        });
    }

    private void sendOrder(String msg, int position) {
        ShipCompanyEntity entity = mList.get(position);
        String code = entity.ship_com_code;
        ShoppingManager.getInstance().getSendShip(mOrder_id,code,msg,new ResponseCallback(){
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                finish();
                EventBus.getDefault().post(new OrderEvent());
            }

            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(ShipCompanyActivity.this);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(ShipCompanyActivity.this);
            }
        });
    }

    private void loadData() {
        ShoppingManager.getInstance().getCompanyShip(new FinalResponseCallback<ShipCompanyEntity>(mRvShipCompany) {
            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(ShipCompanyActivity.this);
            }

            @Override
            public void onSuccess(List<ShipCompanyEntity> t) {
                super.onSuccess(t);
                mList.clear();
                mList.addAll(t);
                ShipCompanyAdapter adapter = new ShipCompanyAdapter(ShipCompanyActivity.this, mList);
                mRvShipCompany.setAdapter(adapter);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(ShipCompanyActivity.this);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }

    @OnClick(R.id.rl_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
