package com.mutoumulao.expo.redwood.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.ConfirmOrderdActivity;
import com.mutoumulao.expo.redwood.adapter.ShoppingAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.CheckClickListener;
import com.mutoumulao.expo.redwood.entity.custom_interface.InnerRecylerViewListener;
import com.mutoumulao.expo.redwood.entity.event.ShoppingCarEvent;
import com.mutoumulao.expo.redwood.logic.ShoppingManager;
import com.mutoumulao.expo.redwood.util.LogUtils;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by lzy on 2018/8/1.
 */

public class ShoppingFragment extends BaseFragment implements View.OnClickListener, CheckClickListener {
    private RelativeLayout mRlBack;
    private TextView mTvTitle;
    private BaseRecyclerView mRvShopping;
    private CheckBox mCbCheck;
    private RelativeLayout mRlCheck;
    private TextView mTv_pay;

    private List<ShoppingEntity> mList = new ArrayList<>();
    private ShoppingAdapter mAdapter;
    List<ShoppingEntity> list_to = new ArrayList<>();
    private int mLength;
    private boolean[] mBooleansArrays;
    private TextView mTv_price;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping, null);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mRlBack = (RelativeLayout) findViewById(R.id.rl_back);
        final ScrollView sc = (ScrollView) findViewById(R.id.sc);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRvShopping = (BaseRecyclerView) findViewById(R.id.rv_shopping);
        mCbCheck = (CheckBox) findViewById(R.id.cb_check);
        mRlCheck = (RelativeLayout) findViewById(R.id.rl_check);
        mTv_pay = (TextView) findViewById(R.id.tv_pay);
        mTv_price = (TextView) findViewById(R.id.tv_price);

        sc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIUtil.hideSoftInput(mBaseActivity,sc);
                return false;
            }
        });

        mRlBack.setVisibility(View.GONE);
        mTv_pay.setOnClickListener(this);
        mRlCheck.setOnClickListener(this);
        mTvTitle.setText("购物车");
        mCbCheck.setChecked(false);
        mAdapter = new ShoppingAdapter(mBaseActivity, mList);
        mAdapter.mAdapter.setCheckClickListener(this);

        mAdapter.setOnInnerRecylerViewClickView(new InnerRecylerViewListener() {
            @Override
            public void onReduceItemListener(final int out_position, final int inner_position) {
                UIUtil.showConfirm(mBaseActivity, "确认删除吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingManager.getInstance().getShopCarDel(mList.get(out_position).list.get(inner_position).id, new ResponseCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                loadData();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        loadData();
    }


    public void loadData() {
        list_to.clear();
        ShoppingManager.getInstance().getShopcarList(new FinalResponseCallback<ShoppingEntity>(this) {
            @Override
            public void onStart() {
                super.onStart();
                UIUtil.showProgressBar(mBaseActivity);
            }

            @Override
            public void onSuccess(List<ShoppingEntity> t) {
                super.onSuccess(t);
                mList.clear();
                mList.addAll(t);
                mRvShopping.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                UIUtil.hideProgressBar(mBaseActivity);
            }

            @Override
            public boolean isShowNotice() {
                return super.isShowNotice();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay:
                if (list_to.size() == 0 || list_to == null) {
                    UIUtil.toastShort(mBaseActivity, "请选择商品");
                    return;
                }
                paraseData();
                Intent intent = new Intent(mBaseActivity, ConfirmOrderdActivity.class);
                intent.putExtra("good_list", (Serializable) list_to);
                intent.putExtra("is_single", false);
                startActivity(intent);
                break;
            case R.id.rl_check:
                if (mCbCheck.isChecked()) {
                    mCbCheck.setChecked(false);
                    Set<Integer> set = mAdapter.mAdapter.isSelected.keySet();
                    Iterator<Integer> iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Integer keyId = iterator.next();
                        mAdapter.mAdapter.isSelected.put(keyId, false);
                    }
                    for (int j = 0; j < mList.size(); j++) {
                        for (int i = 0; i < mList.get(j).list.size(); i++) {
                            mAdapter.mAdapter.isCheckedArray[i] = false;
                        }
                    }
                } else {
                    mCbCheck.setChecked(true);
                    Set<Integer> set = mAdapter.mAdapter.isSelected.keySet();
                    Iterator<Integer> iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Integer keyId = iterator.next();
                        mAdapter.mAdapter.isSelected.put(keyId, true);
                    }
                    for (int j = 0; j < mList.size(); j++) {
                        for (int i = 0; i < mList.get(j).list.size(); i++) {
                            mAdapter.mAdapter.isCheckedArray[i] = true;
                        }
                    }
                }
                mAdapter.mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(ShoppingCarEvent shoppingCarEvent) {
        loadData();
        list_to.clear();
    }


    @Override
    public void onCheckClickListener(boolean[] isCheckedArray) {
        mLength = isCheckedArray.length;
        mBooleansArrays = isCheckedArray;
        paraseData();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadData();
        Window window = mBaseActivity.getWindow();
        //如果系统5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.tc_write));
        }
    }

    private void paraseData() {
        list_to.clear();
        double prices = 0.0;
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mLength; j++) {
                if (mBooleansArrays[j]) {
                    ShoppingEntity entity = new ShoppingEntity();
                    entity.id = mList.get(i).id;
                    entity.avatar = mList.get(i).avatar;
                    entity.business_name = mList.get(i).business_name;
                    List<ShoppingEntity.ShoppingCarEntity> list_goods = new ArrayList<>();
                    list_goods.add(mList.get(i).list.get(j));

                    prices = prices +
                            Double.parseDouble(TextUtils.isEmpty(mList.get(i).list.get(j).num) ? "1" : mList.get(i).list.get(j).num) *
                                    Double.parseDouble(TextUtils.isEmpty(mList.get(i).list.get(j).price) ? "0" : mList.get(i).list.get(j).price);
                    entity.setList(list_goods);
                    list_to.add(entity);
                }
            }
        }
        mTv_price.setText("合计:" + prices);
        LogUtils.d(list_to.size() + "");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
