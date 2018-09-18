package com.mutoumulao.expo.redwood.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.adapter.GoodsSpecCommonAdapter;
import com.mutoumulao.expo.redwood.adapter.GoodsSpecSelfAdapter;
import com.mutoumulao.expo.redwood.adapter.GoodsSpecTypeNumberAdapter;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.entity.custom_interface.ImageRecyclerReduceItemListener;
import com.mutoumulao.expo.redwood.entity.custom_interface.RecyclerViewAddOneListener;
import com.mutoumulao.expo.redwood.util.UIUtil;
import com.mutoumulao.expo.redwood.view.BaseRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mutoumulao.expo.redwood.util.UIUtil.showAlert;

/**
 * Created by lzy on 2018/8/4.
 */

public class GoodsSpecActivity extends BaseActivity {
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
    @BindView(R.id.tv_together)
    TextView mTvTogether;

    @BindView(R.id.iv_more)
    ImageView mIvMore;

    private String[] typeArray1 = {"乳白色", "蓝色", "黑色", "白色", "青色",
            "银色", "灰色", "桔红色", "西瓜红", "粉红色",
            "明黄色", "荧光黄", "金色", "墨绿色", "米白色",
            "银色", "深灰色", "玫红色", "红色", "卡其色",
            "杏色", "香横色", "浅黄色", "翠绿色", "绿色",
            "天蓝色", "花色", "米黄色"};//, "时间"};
    private List<StoreManagerListEntity.GuigesEntity> mSpecNameSelfList = new ArrayList<>();
    private List<StoreManagerListEntity.GuigesEntity> mSpecNameCommonList = new ArrayList<>();
    private List<StoreManagerListEntity.SkuListEntity> mSpecPriceList = new ArrayList<>();
    private GoodsSpecSelfAdapter mSelfAdapter;
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

        List<StoreManagerListEntity.GuigesEntity> good_guige = (List<StoreManagerListEntity.GuigesEntity>) getIntent().getSerializableExtra("good_guige");
        if (good_guige != null) {
            for(int i=0;i<good_guige.size();i++) {
                boolean isCommon = false;
                for(int j=0;j<typeArray1.length;j++){
                    if(typeArray1[j].equals(good_guige.get(i).title)){
                        mSpecNameCommonList.add(good_guige.get(i));
                        isCommon = true;
                        break;
                    }
                }
                if(!isCommon){
                    good_guige.get(i).selfFlag = true;
                    mSpecNameSelfList.add(good_guige.get(i));
                }
       /*         if (good_guige.get(i).selfFlag)
                    mSpecNameSelfList.add(good_guige.get(i));
                else
                    mSpecNameCommonList.add(good_guige.get(i));*/
            }
        }
        List<StoreManagerListEntity.SkuListEntity> good_spec = (List<StoreManagerListEntity.SkuListEntity>) getIntent().getSerializableExtra("good_spec");
        if (good_spec != null) {
            mSpecPriceList.addAll(good_spec);
        }


        mCommonAdapter = new GoodsSpecCommonAdapter(this,mSpecNameCommonList);
        mRvComment.setAdapter(mCommonAdapter);
        mCommonAdapter.notifyDataSetChanged();
        mTvTitle.setText("商品规格");
        mRlRight.setVisibility(View.VISIBLE);
        mTvRight.setText("完成");
        mSelfAdapter = new GoodsSpecSelfAdapter(this, mSpecNameSelfList);
        mRvSpec.setAdapter(mSelfAdapter);

        mNumberAdapter = new GoodsSpecTypeNumberAdapter(this, mSpecPriceList);
        mRvPrice.setAdapter(mNumberAdapter);
        mRvPrice.setNestedScrollingEnabled(false);
        mSelfAdapter.setOnItemDeleteListener(new ImageRecyclerReduceItemListener() {
            @Override
            public void onReduceItemListener(int position) {
                if(mSpecNameSelfList!=null&&mSpecNameSelfList.size()>position) {
                    minusPriceAndNumberItem(mSpecNameSelfList.get(position).title);
                    mSpecNameSelfList.remove(position);
                    mNumberAdapter.notifyDataSetChanged();
                    mSelfAdapter.notifyDataSetChanged();
                    //mSelfAdapter.notifyItemChanged(mSelfAdapter.getItemCount());
                    return;
                }
                //initPriceAndNumber();
            }
        });
        mSelfAdapter.setAddItem(new RecyclerViewAddOneListener() {
            @Override
            public void onAddItemListener(String entity, int position) {
                StoreManagerListEntity.GuigesEntity bean = new StoreManagerListEntity.GuigesEntity();
                bean.title = entity;
                bean.selfFlag = true;
                if(mSpecNameSelfList.size()>position) {
                    updatePriceAndNumberItem(mSpecNameSelfList.get(position).title,entity);
                    mSpecNameSelfList.get(position).title = entity;
                }
                else {
                    mSpecNameSelfList.add(bean);
                    addPriceAndNumberItem(bean.title);
                }
                mSelfAdapter.notifyItemChanged(position);
                mSelfAdapter.notifyItemChanged(mSpecNameSelfList.size());

                mNumberAdapter.notifyDataSetChanged();

            }
        });

        mCommonAdapter.setOnItemDeleteListener(new ImageRecyclerReduceItemListener() {
            @Override
            public void onReduceItemListener(int position) {
                for(int i=mSpecNameCommonList.size()-1;i>=0;i--){
                    if(mSpecNameCommonList.get(i).title.equals(typeArray1[position])){
                        minusPriceAndNumberItem(mSpecNameCommonList.get(i).title);
                        mSpecNameCommonList.remove(i);
                        mNumberAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                //initPriceAndNumber();
                //mNumberAdapter.notifyDataSetChanged();

            }
        });
        mCommonAdapter.setAddItem(new RecyclerViewAddOneListener() {
            @Override
            public void onAddItemListener(String entity, int position) {
                StoreManagerListEntity.GuigesEntity bean = new StoreManagerListEntity.GuigesEntity();
                bean.title = typeArray1[position];
                bean.selfFlag = false;
                mSpecNameCommonList.add(bean);
                mCommonAdapter.notifyDataSetChanged();
                //initPriceAndNumber();
                addPriceAndNumberItem(bean.title);

                mNumberAdapter.notifyDataSetChanged();

            }
        });

        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreFlag = !moreFlag;
                if(moreFlag){
                    mIvMore.setImageResource(R.drawable.icon_up);
                }else {
                    mIvMore.setImageResource(R.drawable.icon_down);

                }
                mCommonAdapter.moreFlag = moreFlag;
                mCommonAdapter.notifyDataSetChanged();
            }
        });
        mTvTogether.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSpecPriceList!=null&&mSpecPriceList.size()>0) {
                    showWindows();
                }else {
                    UIUtil.toastShort(GoodsSpecActivity.this, "请选择属性");
                }
            }
        });


    }


    private void showWindows() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_confirm_together, null);
        final Dialog dialog = showAlert(this, view);
        final EditText priceText = (EditText) view.findViewById(R.id.et_confirm_price);
        final EditText numberText = (EditText) view.findViewById(R.id.et_confirm_number);
        Button leftBtn = (Button) view.findViewById(R.id.btn_confirm_left);
        Button rightBtn = (Button) view.findViewById(R.id.btn_confirm_right);
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
                String price = priceText.getText().toString();
                String number = numberText.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    UIUtil.toastShort(GoodsSpecActivity.this, "请输入价格");
                    return;
                }
                if (TextUtils.isEmpty(number)){
                    UIUtil.toastShort(GoodsSpecActivity.this, "请输入数量");
                    return;
                }
                for(int i=0;i<mSpecPriceList.size();i++){
                    mSpecPriceList.get(i).price = price;
                    mSpecPriceList.get(i).stock = number;
                }
                mNumberAdapter.notifyDataSetChanged();
            }
        });
    }

    boolean moreFlag;

    public void addPriceAndNumberItem(String title){
        StoreManagerListEntity.SkuListEntity serverEntity = new StoreManagerListEntity.SkuListEntity();
        serverEntity.spec = title;
        serverEntity.sku_name = serverEntity.spec;
        mSpecPriceList.add(serverEntity);
    }

    public void minusPriceAndNumberItem(String title){
        for(int i=0;i<mSpecPriceList.size();i++){
            if(title.equals(mSpecPriceList.get(i).spec)) {
                mSpecPriceList.remove(i);
                return;
            }
        }
    }

    public void updatePriceAndNumberItem(String titleOld,String titleNew){
        for(int i=0;i<mSpecPriceList.size();i++){
            if(titleOld.equals(mSpecPriceList.get(i).spec)) {
                mSpecPriceList.get(i).sku_name = titleNew;
                mSpecPriceList.get(i).spec = titleNew;
                return;
            }
        }
    }

/*

    public void initPriceAndNumber() {
        mSpecPriceList.clear();
        //String sku_name = "";
        for (int i = 0; mSpecNameSelfList != null && i < mSpecNameSelfList.size(); i++) {
*/
/*            if (i < mSpecNameSelfList.size() - 1) {
                sku_name = sku_name + mSpecNameSelfList.get(i).title + ",";
            } else {
                sku_name = sku_name + mSpecNameSelfList.get(i).title;
            }*//*

            StoreManagerListEntity.SkuListEntity serverEntity = new StoreManagerListEntity.SkuListEntity();
            serverEntity.spec = mSpecNameSelfList.get(i).title;
            serverEntity.sku_name = serverEntity.spec;
            mSpecPriceList.add(serverEntity);
        }

        for (int i = 0; mSpecNameCommonList != null && i < mSpecNameCommonList.size(); i++) {
*/
/*            if (i < mSpecNameCommonList.size() - 1) {
                sku_name = sku_name + mSpecNameCommonList.get(i).title + ",";
            } else {
                sku_name = sku_name + mSpecNameCommonList.get(i).title;
            }*//*

            StoreManagerListEntity.SkuListEntity serverEntity = new StoreManagerListEntity.SkuListEntity();
            serverEntity.spec = mSpecNameCommonList.get(i).title;
            serverEntity.sku_name = serverEntity.spec;
            mSpecPriceList.add(serverEntity);
        }

        mNumberAdapter.notifyDataSetChanged();

    }
*/


    @OnClick({R.id.rl_back, R.id.tv_add, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                UIUtil.showConfirm(this, "返回将清空数据，是否确定？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpecPriceList.clear();
                        mSpecNameCommonList.clear();
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
        mSpecNameSelfList.addAll(mSpecNameCommonList);
        intent.putExtra("good_guige", (Serializable) (mSpecNameSelfList));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addList() {

        String name = mEtName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {

            StoreManagerListEntity.GuigesEntity entity = new StoreManagerListEntity.GuigesEntity();
            entity.title = name;
            mSpecNameSelfList.add(entity);
            mSelfAdapter.notifyDataSetChanged();
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
