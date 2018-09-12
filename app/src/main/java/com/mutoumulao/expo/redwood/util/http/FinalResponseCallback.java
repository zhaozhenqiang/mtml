package com.mutoumulao.expo.redwood.util.http;

import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mutoumulao.expo.redwood.R;
import com.mutoumulao.expo.redwood.base.BaseFragment;
import com.mutoumulao.expo.redwood.base.CommonActivity;
import com.mutoumulao.expo.redwood.util.BaseConstant;
import com.mutoumulao.expo.redwood.util.NetworkUtil;
import com.mutoumulao.expo.redwood.util.UIUtil;

import java.util.List;


/**
 * 带UI逻辑的回调
 * FinalResponseCallback
 * chenbo
 * 2015年3月13日 下午12:56:40
 *
 * @version 1.0
 */
public class FinalResponseCallback<T> extends ResponseCallback<T> {

    private BaseFragment mFragment;
    private CommonActivity mActivity;
    private ViewGroup mParent;

    private boolean mIsEmpty;
    private int mLoadingType;

    public FinalResponseCallback(BaseFragment fragment) {
        this(fragment, BaseConstant.TYPE_INIT);
    }

    public FinalResponseCallback(CommonActivity activity) {
        this(activity, BaseConstant.TYPE_INIT);
    }

    public FinalResponseCallback(ViewGroup parent) {
        this(parent, BaseConstant.TYPE_INIT);
    }

    public FinalResponseCallback(BaseFragment fragment, int loadingType) {
        super();
        mFragment = fragment;
        mLoadingType = loadingType;
        init();
    }

    public FinalResponseCallback(CommonActivity activity, int loadingType) {
        super();
        mActivity = activity;
        mLoadingType = loadingType;
        init();
    }

    public FinalResponseCallback(ViewGroup parent, int loadingType) {
        super();
        mParent = parent;
        mLoadingType = loadingType;
        init();
    }


    private void init() {
        if (mParent == null) {
            if (mActivity != null) {
                try {
                    mParent = (ViewGroup) mActivity.getContentView();
                } catch (Exception e) {
                }
            } else if (mFragment != null) {
                try {
                    mParent = (ViewGroup) mFragment.getRootView();
                } catch (Exception e) {
                }
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mLoadingType == BaseConstant.TYPE_INIT) {
            init();
            if (!isFinish()) {
                UIUtil.showLoadingView(mParent, getLoadingStr());
            }
        }
    }

    @Override
    public void onFailure(Exception error) {
        super.onFailure(error);
        String errorStr = getErrorStr();
        int errorCode = 0;
        if (error instanceof HttpException) {
            errorStr = error.getMessage();
            errorCode = ((HttpException) error).getExceptionCode();
        }
        if (mLoadingType == BaseConstant.TYPE_INIT) {
            init();
            if (!isFinish()) {
                int resId;
                if (!isNetworkAvailable()) {
                    resId = R.drawable.ic_no_network;
                    errorStr = mParent.getResources().getString(R.string.notice_no_network);
                } else {
                    resId = getErrorResId(errorCode);
                }
                UIUtil.showErrorView(mParent, errorStr, resId, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onReload();
                    }
                });
            }
        }
    }

    @Override
    public void onSuccess(T t) {
        super.onSuccess(t);
        /*if (mLoadingType != TYPE_NEXT) {
            init();
            if (!isFinish()) {
                UIUtil.hideAllNoticeView(mParent);
            }
        }*/
        init();
        if (!isFinish()) {
            UIUtil.hideAllNoticeView(mParent);
        }
        mIsEmpty = false;
    }

    @Override
    public void onSuccess(List<T> t) {
        super.onSuccess(t);
        /*if (mLoadingType != TYPE_NEXT) {
            init();
            if (!isFinish()) {
                UIUtil.hideAllNoticeView(mParent);
            }
        }*/
        init();
        if (!isFinish()) {
            UIUtil.hideAllNoticeView(mParent);
        }
        mIsEmpty = false;
    }

    /**
     * 重新加载
     * onReload
     *
     * @since 1.0
     */
    public void onReload() {

    }

    /**
     * 设置返回的数据为空时候的显示页面
     * setIsEmpty
     *
     * @param isEmpty
     * @since 1.0
     */
    public void setIsEmpty(boolean isEmpty) {
        setIsEmpty(isEmpty, "");
    }

    /**
     * 设置返回的数据为空时候的显示页面
     * setIsEmpty
     *
     * @param isEmpty
     * @param emptyStr 设置为空时候，提示的字符串
     * @since 1.0
     */
    public void setIsEmpty(boolean isEmpty, String emptyStr) {
        mIsEmpty = isEmpty;
        if (mIsEmpty) {
            init();
            if (!isFinish()) {
                UIUtil.showEmptyView(mParent, emptyStr, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onReload();
                    }
                });
            }
        }
    }

    /**
     * 在制定的parent里显示数据为空提示，需要在recycleview的上面覆盖parent view
     *
     * @param isEmpty
     * @param parent
     * @param emptyStr
     */
    public void setIsEmpty(boolean isEmpty, ViewGroup parent, String emptyStr, int resId) {
        mIsEmpty = isEmpty;
        parent.setVisibility(View.GONE);

        if (mIsEmpty) {
            parent.setVisibility(View.VISIBLE);

            if (!isFinish()) {
                UIUtil.showEmptyView(parent, emptyStr, resId, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onReload();
                    }
                });
            }
        }
    }

    /**
     * 设置返回的数据为空时候的显示页面
     * setIsEmpty
     *
     * @param isEmpty
     * @param emptyStr 设置为空时候，提示的字符串
     * @since 1.0
     */
    public void setIsEmpty(boolean isEmpty, String emptyStr, int resId) {
        mIsEmpty = isEmpty;
        if (mIsEmpty) {
            init();
            if (!isFinish()) {
                UIUtil.showEmptyView(mParent, emptyStr, resId, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onReload();
                    }
                });
            }
        }
    }


    /**
     * 设置返回的数据为空时候的显示页面
     *
     * @param isEmpty
     * @param emptyView
     */
    public void setIsEmpty(boolean isEmpty, View emptyView) {
        mIsEmpty = isEmpty;
        if (mIsEmpty) {
            init();
            if (!isFinish()) {
                UIUtil.showEmptyView(mParent, emptyView, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onReload();
                    }
                });
            }
        }
    }

    @Override
    public boolean isShowNotice() {
        return false;
    }

    /**
     * 设置加载失败的时候，提示的字符串
     * getErrorStr
     *
     * @since 1.0
     */
    public String getErrorStr() {
        return "";
    }

    public int getErrorResId(int errCode) {
        return 0;
    }

    /**
     * 设置加载中的字符串
     *
     * @since 1.0
     */
    public String getLoadingStr() {
        return "";
    }

    // 判断当前页面是否已经结束
    private boolean isFinish() {
        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed()) {
                return true;
            }
            return mActivity.isFinishing();
        } else if (mFragment != null) {
            return mFragment.getActivity() == null;
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        if (mParent != null) {
            return NetworkUtil.isNetworkAvailable(mParent.getContext());
        }
        return true;
    }

}
