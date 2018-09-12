package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzy on 2018/8/30.
 */

public class UserGuideActivity extends BaseActivity {


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
    @BindView(R.id.tv_content01)
    TextView mTvContent01;
    @BindView(R.id.ll_01)
    LinearLayout mLl01;
    @BindView(R.id.tv_content02)
    TextView mTvContent02;
    @BindView(R.id.ll_02)
    LinearLayout mLl02;
    @BindView(R.id.tv_content03)
    TextView mTvContent03;
    @BindView(R.id.ll_03)
    LinearLayout mLl03;

    private String[] desc = {
            "\r平台提供代购服务，我们从光身到成品喷漆运输一系列过程都是平台一手跟踪完成，从选货到最后成品都是我们平台亲自把关。保证客户在购买方面有全新的体验和产品质量的保证，并且只收取3%的代购费率。",
            "\r如您的手机收不到验证码，请[联系客服]将手机号反馈给我们，平台将在1个工作日内为您处理;或者直接拨打客服热线，我们的客服将竭诚为您服务。",
            "\r打开木头木佬app，在“我的”页面点击重置密码，输入注册时的手机号码，再点击获取验证码，收到验证码后，输入验证码，再输入新密码，点击确认即可。"};

    private String[] titles = {
            "什么是代购服务",
            "手机号收不到验证码",
            "如何修改登录密码"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        ButterKnife.bind(this);
        initView();
        mTvTitle.setText("用户指南");
    }

    private void initView() {
        mTvContent01.setText(titles[0]);
        mTvContent02.setText(titles[1]);
        mTvContent03.setText(titles[2]);

    }

    @OnClick({R.id.rl_back, R.id.ll_01, R.id.ll_02, R.id.ll_03})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_01:
                intent = new Intent(this,GuideDescActivity.class);
                intent.putExtra("title",titles[0]);
                intent.putExtra("desc",desc[0]);
                startActivity(intent);
                break;
            case R.id.ll_02:
                intent = new Intent(this,GuideDescActivity.class);
                intent.putExtra("title",titles[1]);
                intent.putExtra("desc",desc[1]);
                startActivity(intent);
                break;
            case R.id.ll_03:
                intent = new Intent(this,GuideDescActivity.class);
                intent.putExtra("title",titles[2]);
                intent.putExtra("desc",desc[2]);
                startActivity(intent);
                break;
        }
    }


}
