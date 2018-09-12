package com.mutoumulao.expo.redwood.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.util.AndroidBug5497Workaround;
import com.mutoumulao.expo.redwood.util.ApiCompatibleUtil;
import com.mutoumulao.expo.redwood.util.SystemUtil;

/**
 * Created by lzy on 2018/7/25.
 */

public abstract class CommonActivity extends AppCompatActivity {
    protected Bundle savedInstanceState;
    private RelativeLayout mBaseLayout;
//    protected TitleBar mTitleBar;
    protected View mStatusBarTintView;
    private FrameLayout mContentLayout;
    public boolean mIsResume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        // 竖屏固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    protected  void init(){
        CommonApp.getApp().start();
        // 初始化头部
        initTopView();
        // 初始化布局
        //initViews();
        // 初始化数据
        //initData(savedInstanceState);
    };


    /**
     * 初始化头部
     */
    private void initTopView() {
        if (isUseCustomContent()) {
            // 最外层布局
            mBaseLayout = new RelativeLayout(this);
            if (isShowTitleBar()) {
                initTitleBar();
                // 填入View
//                mBaseLayout.addView(mTitleBar);
            } else if (isShowTintStatusBar() && SystemUtil.isTintStatusBarAvailable(this)) {
                initTintStatusBar();
                // 填入View
                mBaseLayout.addView(mStatusBarTintView);
            }
            // 内容布局
            mContentLayout = new FrameLayout(this);
            mContentLayout.setId(R.id.content);
//            mContentLayout.setPadding(0, 0, 0, ApiCompatibleUtil.hasLollipop() ? SystemUtil.getNavigationBarHeight(this) : 0);
            RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParamsContent.addRule(RelativeLayout.BELOW, R.id.titlebar);
            mBaseLayout.addView(mContentLayout, layoutParamsContent);

            // 设置ContentView
            setContentView(mBaseLayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            if (SystemUtil.isTintStatusBarAvailable(this)) {
                if ((getWindow().getAttributes().softInputMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) != 0) {
                    AndroidBug5497Workaround.assistActivity(this);
                }
            }
        }
    }

    protected void initTitleBar() {
        // 主标题栏
/*        mTitleBar = new TitleBar(this);
        mTitleBar.setId(R.id.titlebar);
        mTitleBar.setBackgroundResource(R.color.colorPrimary);
        mTitleBar.setTitleColor(getResources().getColor(R.color.colorTitleBar));
        mTitleBar.setActionTextColor(getResources().getColor(R.color.colorTitleBar));
        mTitleBar.setLeftImageResource(R.drawable.icon_left);
        mTitleBar.setImmersive(SystemUtil.isTintStatusBarAvailable(this));
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    // 跟布局是否使用自定义布局
    private boolean isUseCustomContent() {
        return isShowTitleBar() || (useTintStatusBar() && ApiCompatibleUtil.hasKitKat());
    }

    /**
     * 是否使用默认titlebar
     * useTitleBar
     */
    protected boolean isShowTitleBar() {
        return true;
    }

    /**
     * 是否使用沉浸式状态栏
     *
     * @return
     */
    protected boolean useTintStatusBar() {
        return true;
    }

    /**
     * 是否使用默认的statusbar
     *
     * @return
     */
    protected boolean isShowTintStatusBar() {
        return true;
    }

    protected void initTintStatusBar() {
        mStatusBarTintView = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SystemUtil.getStatusBarHeight());
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setId(R.id.titlebar);
    }

    /**
     * 获得内容布局
     * getContentView
     */
    public View getContentView() {
        if (isShowTitleBar()) {
            return mContentLayout;
        } else {
            return getWindow().getDecorView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsResume = false;
    }
}
