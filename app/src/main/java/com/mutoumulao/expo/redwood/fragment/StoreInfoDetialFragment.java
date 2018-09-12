package com.mutoumulao.expo.redwood.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
import com.mutoumulao.expo.redwood.logic.MineFuncManager;
import com.mutoumulao.expo.redwood.util.BitmapTools;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;

/**
 * Created by lzy on 2018/8/26.
 */

public class StoreInfoDetialFragment extends BaseFragment {

    private String mStore_id;
    private EditText mEt_name;
    private ImageView mIv_head;
    private EditText mEt_phone;
    private TextView mTv_location;
    private EditText mEt_address;
    private EditText mEt_info;

    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_create_store_info, null);
    }

    @Override
    protected void initViews() {

        RelativeLayout rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        mEt_name = (EditText) findViewById(R.id.et_name);
        LinearLayout ll_avater = (LinearLayout) findViewById(R.id.ll_avater);
        mIv_head = (ImageView) findViewById(R.id.iv_head);
        LinearLayout ll_username = (LinearLayout) findViewById(R.id.ll_username);
        mEt_phone = (EditText) findViewById(R.id.et_phone);
        LinearLayout ll_address = (LinearLayout) findViewById(R.id.ll_address);
        mTv_location = (TextView) findViewById(R.id.tv_location);
        LinearLayout ll_back_message = (LinearLayout) findViewById(R.id.ll_back_message);
        mEt_address = (EditText) findViewById(R.id.et_address);
        mEt_info = (EditText) findViewById(R.id.et_info);
        RecyclerView rv_phone = (RecyclerView) findViewById(R.id.rv_phone);
        TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        Button btn_go = (Button) findViewById(R.id.btn_go);

        rl_title.setVisibility(View.GONE);
        rv_phone.setVisibility(View.GONE);
        tv_desc.setVisibility(View.GONE);
        btn_go.setVisibility(View.GONE);
        mEt_name.setEnabled(false);
        mEt_phone.setEnabled(false);
        mEt_info.setEnabled(false);
        mEt_address.setEnabled(false);

        Bundle arguments = getArguments();
        mStore_id = arguments.getString("store_id");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        MineFuncManager.getInstance().geStoreDetial(mStore_id,new ResponseCallback<StoreEntity>(){
            @Override
            public void onSuccess(StoreEntity storeEntity) {
                super.onSuccess(storeEntity);
                mEt_name.setText(storeEntity.business_name);
                new BitmapTools(mBaseActivity).display(mIv_head, UrlConst.IMAGE_URL + storeEntity.avatar);
                mEt_phone.setText(storeEntity.phone);
                mTv_location.setText(storeEntity.province_name + storeEntity.city_name + storeEntity.area_name);
                mEt_address.setText(storeEntity.address);
                mEt_info.setText(storeEntity.description);
            }

            @Override
            public boolean isShowNotice() {
                return false;
            }
        });
    }
}
