package com.mutoumulao.expo.redwood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.hyphenate.chat.EMClient;
import com.mutoumulao.expo.redwood.MainActivity;
import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseActivity;
import com.mutoumulao.expo.redwood.util.SharedPutils;

/**
 * Created by lzy on 2018/7/25.
 */

public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        forward(1000);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void forward(int sleepTime) {
        mHandler.postDelayed(mForwardRunnable, sleepTime);
    }

    Runnable mForwardRunnable = new Runnable() {
        @Override
        public void run() {
            skipToHomeActivity();
        }
    };

    private void skipToHomeActivity() {
        if (!TextUtils.isEmpty(SharedPutils.getPreferences(this).getUserID())) {
            EMClient.getInstance().chatManager().loadAllConversations();
            EMClient.getInstance().groupManager().loadAllGroups();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mForwardRunnable);
        }
    }
}
