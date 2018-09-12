package com.mutoumulao.expo.redwood.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mutoumulao.expo.redwood.MainActivity;

/**
 * Created by lzy on 2018/7/25.
 */

public abstract class BaseActivity extends CommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 初始化头部
     */
/*    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        mTitleBar.setLeftImageResource(R.drawable.icon_left);
        mTitleBar.setBackgroundResource(R.color.tc_write);
        mTitleBar.setLeftTextColor(getResources().getColor(R.color.tc_black3));
        mTitleBar.setActionTextColor(getResources().getColor(R.color.tc_black3));
        mTitleBar.setTitleColor(getResources().getColor(R.color.tc_black3));
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMain();
                finish();
            }
        });
    }*/

    public boolean toMain() {
        if (isTaskRoot() && !(this instanceof MainActivity)) {
            startActivity(new Intent(BaseActivity.this, MainActivity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //toMain();
        super.onBackPressed();
    }
}
