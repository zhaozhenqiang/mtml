package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GoodsSpecCommonAdapter;
import com.mutoumulao.expo.redwood.adapter.GoodsSpecSelfAdapter;
import com.mutoumulao.expo.redwood.adapter.GoodsSpecTypeNumberAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.RecyclerViewAddItemListener;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecActicity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.rl_right)
    RelativeLayout mRlRight;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.view01)
    View mView01;
    @BindView(R.id.rv_spec_self)
    RecyclerView mRvSpec;
    @BindView(R.id.rv_price)
    BaseRecyclerView mRvPrice;

    @BindView(R.id.rv_spec_common)
    BaseRecyclerView mRvComment;

    private List<StoreManagerListEntity.GuigesEntity> mSpecNameSelfList = new ArrayList<>();
    private List<StoreManagerListEntity.GuigesEntity> mSpecNameCommonList = new ArrayList<>();
    private List<StoreManagerListEntity.SkuListEntity> mSpecPriceList = new ArrayList<>();
    private GoodsSpecSelfAdapter goodsSpecSelfAdapter;
    private GoodsSpecTypeNumberAdapter mNumberAdapter;

    GoodsSpecCommonAdapter mCommonAdapter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_spec);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mCommonAdapter = new GoodsSpecCommonAdapter(this,true);
        mRvComment.setAdapter(mCommonAdapter);
        mCommonAdapter.notifyDataSetChanged();
        mTvTitle.setText("商品规格");
        mRlRight.setVisibility(View.VISIBLE);
        mTvRight.setText("完成");
        goodsSpecSelfAdapter = new GoodsSpecSelfAdapter(this, mSpecNameSelfList);
        mRvSpec.setAdapter(goodsSpecSelfAdapter);

        mNumberAdapter = new GoodsSpecTypeNumberAdapter(this, mSpecPriceList);
        mRvPrice.setAdapter(mNumberAdapter);
        mRvPrice.setNestedScrollingEnabled(false);
        goodsSpecSelfAdapter.setAddItem(new RecyclerViewAddItemListener() {
            @Override
            public void onAddItemListener(List<String> entity, int position) {
                mSpecPriceList.clear();
                String sku_name = "";
                for (int i = 0; mSpecNameSelfList!=null&&i < mSpecNameSelfList.size(); i++) {
                    if (i < mSpecNameSelfList.size() - 1) {
                        sku_name = sku_name + mSpecNameSelfList.get(i).title + ",";
                    } else {
                        sku_name = sku_name + mSpecNameSelfList.get(i).title;
                    }
                }
                if (entity != null) {
                    for (int i = 0; i < entity.size(); i++) {
                        StoreManagerListEntity.SkuListEntity serverEntity = new StoreManagerListEntity.SkuListEntity();
                        serverEntity.spec = entity.get(i);
                        serverEntity.sku_name = sku_name;
                        mSpecPriceList.add(serverEntity);
                    }
                }
                mNumberAdapter.notifyDataSetChanged();
            }
        });

        List<StoreManagerListEntity.GuigesEntity> good_guige = (List<StoreManagerListEntity.GuigesEntity>) getIntent().getSerializableExtra("good_guige");
        if (good_guige != null) {
            mSpecNameSelfList.addAll(good_guige);
        }
        List<StoreManagerListEntity.SkuListEntity> good_spec = (List<StoreManagerListEntity.SkuListEntity>) getIntent().getSerializableExtra("good_spec");
        if (good_spec != null) {
            mSpecPriceList.addAll(good_spec);
            /*for (int i = 0; i < good_spec.size(); i++) {
                GoodsSpecNameEntity entity = new GoodsSpecNameEntity();
                String pre_name = good_spec.get(0).sku_name;
                entity.name  = good_spec.get(0).sku_name;
                entity.info_name.add(good_spec.get(0).spec);
                int b = i + 1;
                if (b < good_spec.size()) {
//                    b++;
                    String old_name = good_spec.get(b).sku_name;
                    if (pre_name.equals(old_name)) {
                        entity.info_name.add(good_spec.get(b).spec);
                    }
                }
                mSpecNameSelfList.add(entity);
            }
            goodsSpecSelfAdapter.notifyDataSetChanged();
            */
        }

    }

    @OnClick({R.id.rl_back, R.id.tv_add, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                UIUtil.showConfirm(this, "返回将清空数据，是否确定？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpecPriceList.clear();
                        mSpecNameSelfList.clear();
                        Intent intent = new Intent();
                        intent.putExtra("good_spec", (Serializable) mSpecPriceList);
                        intent.putExtra("good_guige", (Serializable) mSpecNameSelfList);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
            case R.id.tv_add:
                addList();
                break;
            default:
                break;
            case R.id.rl_right:
                toComplete();
                break;
        }
    }

    private void toComplete() {
        for (int i = 0; i < mSpecPriceList.size(); i++) {
            if (TextUtils.isEmpty(mSpecPriceList.get(i).price)) {
                UIUtil.toastShort(this, "请输入价格");
                return;
            }
            if (TextUtils.isEmpty(mSpecPriceList.get(i).stock)) {
                UIUtil.toastShort(this, "请输入库存");
                return;
            }
        }
        Intent intent = new Intent();
        intent.putExtra("good_spec", (Serializable) mSpecPriceList);
        intent.putExtra("good_guige", (Serializable) mSpecNameSelfList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addList() {

        String name = mEtName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {

            StoreManagerListEntity.GuigesEntity entity = new StoreManagerListEntity.GuigesEntity();
            entity.title = name;
            mSpecNameSelfList.add(entity);
            goodsSpecSelfAdapter.notifyDataSetChanged();
            mEtName.setText("");
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            UIUtil.toastShort(this, "请输入规格名称");
        }
    }


    public void saveEditData(int position, String type, String str) {

        try {
            if ("1".equals(type)) {
                mSpecPriceList.get((position - 1) / 100).price = str;
            } else if ("2".equals(type)) {
                mSpecPriceList.get((position - 2) / 100).stock = str;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
