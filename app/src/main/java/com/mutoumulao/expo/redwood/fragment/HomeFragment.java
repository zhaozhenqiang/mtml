package com.mutoumulao.expo.redwood.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.activity.HomeFuncActivity;
import com.mutoumulao.expo.redwood.activity.HomeMaterialsActivity;
import com.mutoumulao.expo.redwood.activity.HomeServerActivity;
import com.mutoumulao.expo.redwood.adapter.BaseIndicatorAdapter;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.util.DisplayUtil;
import com.mutoumulao.expo.redwood.view.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 2018/8/1.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public static String EXTRCLE_TITLE = "name";

    private EditText mEt_search;
    private RelativeLayout mRl_serveice;
    private RelativeLayout mRl_material;
    private RelativeLayout mRl_invite;
    private RelativeLayout mRl_job_wanted;
    private TabPageIndicator mPageindicator;
    private ViewPager mViewPager;

    private String[] titles = {"推荐", "热门", "最新"};
    private ImageView mIv_search;
    private ImageView mIv_top;

    private RelativeLayout mRl_top;
    private LinearLayout mLl_main;
    private boolean isFirst;


    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void initViews() {

//        mSc = (ScrollView) findViewById(R.id.sc);

        mLl_main = (LinearLayout) findViewById(R.id.ll_main);
        mEt_search = (EditText) findViewById(R.id.et_search);
        mIv_search = (ImageView) findViewById(R.id.iv_search);
        mIv_top = (ImageView) findViewById(R.id.iv_top);
        mRl_serveice = (RelativeLayout) findViewById(R.id.rl_serveice);
        mRl_material = (RelativeLayout) findViewById(R.id.rl_material);
        mRl_top = (RelativeLayout) findViewById(R.id.rl_top);
        mRl_invite = (RelativeLayout) findViewById(R.id.rl_invite);
        mRl_job_wanted = (RelativeLayout) findViewById(R.id.rl_job_wanted);
        mPageindicator = (TabPageIndicator) findViewById(R.id.pageindicator);
        mViewPager = (ViewPager) findViewById(R.id.pager_news);

        mRl_serveice.setOnClickListener(this);
        mRl_material.setOnClickListener(this);
        mRl_invite.setOnClickListener(this);
        mRl_job_wanted.setOnClickListener(this);
        mIv_search.setOnClickListener(this);

        mEt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEt_search.getText().toString().trim();
                    Intent intent = new Intent(mBaseActivity, HomeMaterialsActivity.class);
                    intent.putExtra(EXTRCLE_TITLE, "家具");
                    intent.putExtra("keyword", keyword);
                    intent.putExtra("position", 0);
                    startActivity(intent);

                 /*   if (!TextUtils.isEmpty(keyword)) {
                    } else {
                        UIUtil.toastShort(mBaseActivity, "请输入关键字");
                    }*/
                }
                return false;
            }
        });

        mLl_main.setOnTouchListener(new MyTouchListener());
        mPageindicator.setOnTouchListener(new MyTouchListener());

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        List<Fragment> list = new ArrayList<>();

        HomesMessageFragment frament1 = new HomesMessageFragment();
        HomesMessageFragment framnet2 = new HomesMessageFragment();
        HomesMessageFragment framnet3 = new HomesMessageFragment();
        list.add(frament1);
        list.add(framnet2);
        list.add(framnet3);

        mViewPager.setOffscreenPageLimit(2);
        BaseIndicatorAdapter adapter = new BaseIndicatorAdapter(getChildFragmentManager(), list, titles);
        mViewPager.setAdapter(adapter);
        mPageindicator.setViewPager(mViewPager);
        setTabPagerIndicator();
    }

    private void setTabPagerIndicator() {
        mPageindicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_NOWEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式
        mPageindicator.setDividerColor(mBaseActivity.getResources().getColor(R.color.bg_line));// 设置分割线的颜色
        mPageindicator.setDividerPadding(DisplayUtil.dip2px(mBaseActivity, 10));
        mPageindicator.setIndicatorColor(mBaseActivity.getResources().getColor(R.color.tc_red));// 设置底部导航线的颜色
        mPageindicator.setTextColorSelected(mBaseActivity.getResources().getColor(R.color.tc_red));// 设置tab标题选中的颜色
        mPageindicator.setTextColor(mBaseActivity.getResources().getColor(R.color.tc_black6));// 设置tab标题未被选中的颜色
        mPageindicator.setTextSize(DisplayUtil.sp2px(mBaseActivity, 14));// 设置字体大小
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_serveice:
                intent = new Intent(mBaseActivity, HomeServerActivity.class);
                intent.putExtra(EXTRCLE_TITLE, "服务");
//                intent.putExtra("", "");
                startActivity(intent);
                break;
            case R.id.rl_material:
                intent = new Intent(mBaseActivity, HomeMaterialsActivity.class);
                intent.putExtra(EXTRCLE_TITLE, "原材");
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            case R.id.rl_invite:
                intent = new Intent(mBaseActivity, HomeFuncActivity.class);
                intent.putExtra(EXTRCLE_TITLE, "求职");
//                intent.putExtra("", "");
                startActivity(intent);
                break;
            case R.id.rl_job_wanted:
                intent = new Intent(mBaseActivity, HomeFuncActivity.class);
                intent.putExtra(EXTRCLE_TITLE, "招聘");
//                intent.putExtra("", "");
                startActivity(intent);
                break;

            case R.id.iv_search:
                String keyword = mEt_search.getText().toString().trim();
                intent = new Intent(mBaseActivity, HomeMaterialsActivity.class);
                intent.putExtra(EXTRCLE_TITLE, "家具");
                intent.putExtra("position", 0);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
//                    EventBus.getDefault().post(new HomeSearchEvent(keyword));
                /*if (!TextUtils.isEmpty(keyword)) {
                } else {
                    UIUtil.toastShort(mBaseActivity, "请输入关键字");
                }*/
                break;

            default:
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = mBaseActivity.getWindow();
        //如果系统5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.tc_write));
        }
    }

    class MyTouchListener implements View.OnTouchListener {

        private float mRawX;
        private float mRawY;
        private int mRawX1;
        private int mRawY1;
        private int up;
        private boolean isClick;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mRawX = event.getRawX();
                    mRawY = event.getRawY();

                    mRawX1 = (int) event.getRawX();
                    mRawY1 = (int) event.getRawY();
                    if (Math.abs(up) == DisplayUtil.dip2px(mBaseActivity, 120)) {
                        return false;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) event.getRawX();
                    int moveY = (int) event.getRawY();

                    int dX = moveX - mRawX1;
                    int dY = moveY - mRawY1;


                    if (dY < 0 && Math.abs(dY) < DisplayUtil.dip2px(mBaseActivity, 120)) {
                        mLl_main.scrollTo(0, dY <= DisplayUtil.dip2px(mBaseActivity, 120) ? -dY : DisplayUtil.dip2px(mBaseActivity, 120));
                        if (Math.abs(dY) == DisplayUtil.dip2px(mBaseActivity, 120)){
                            if (isFirst) {
                                up = dY;
                                isFirst =false;
                            }
                        }


                    } else if (Math.abs(up) == DisplayUtil.dip2px(mBaseActivity, 120)) {
                        mRl_top.setVisibility(View.GONE);
                        mLl_main.setOnTouchListener(null);
                        mPageindicator.setFocusable(false);
                        mPageindicator.setOnTouchListener(null);

                    } else {
                        mLl_main.setFocusable(false);

                    }
                    break;

                case MotionEvent.ACTION_UP:
                    float upX2 = event.getRawX();
                    float upY2 = event.getRawY();

                    if (Math.abs(mRawX - upX2) < 10 && Math.abs(mRawY - upY2) < 10) {
                        isClick = false;// 距离较小，当作click事件来处理
                    } else {
                        if (Math.abs(up) == DisplayUtil.dip2px(mBaseActivity, 120)) {
                            isClick = false;// 距离较小，当作click事件来处理
                            mRl_top.setVisibility(View.GONE);
                            mLl_main.setFocusable(false);
                            mLl_main.setOnTouchListener(null);
                            mPageindicator.setFocusable(false);
                            mPageindicator.setOnTouchListener(null);
                        } else {
                            isClick = true;
                            mLl_main.setFocusable(true);
                        }
                    }
                    break;
                default:
                    break;
            }
            return isClick;
        }

    }
}
