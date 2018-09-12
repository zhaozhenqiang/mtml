package com.mutoumulao.expo.redwood.util.http;

import com.mutoumulao.expo.redwood.base.CommonApp;
import com.mutoumulao.expo.redwood.util.UIUtil;

import java.util.List;

/**
 * 类描述：
 * 创建人：Bob
 * 创建时间：2016/7/20 14:10
 */
public class ResponseCallback<T> extends BaseCallback<T> {
    public boolean mSuccess;

    @Override
    public void onSuccess(T t) {
        mSuccess = true;
    }

    @Override
    public void onSuccess(List<T> list) {
        super.onSuccess(list);
        mSuccess = true;
    }

    @Override
    public void onFailure(Exception e) {
        mSuccess = false;
        if (isShowNotice()) {
            if (e instanceof HttpException) {
                UIUtil.toastShort(CommonApp.getApp(), e.getMessage());
            } else {
                UIUtil.toastShort(CommonApp.getApp(), ERR_MSG);
            }
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        super.onResponse(response);
        if (isShowNotice() && response.isSuccess()) {
            UIUtil.toastShort(CommonApp.getApp(), response.msg);
        }
    }

    public boolean isShowNotice() {
        return true;
    }

    public interface Action1<T> {
        void call(T e);
    }

    public static <T> ResponseCallback<T> map(final ResponseCallback<T> callback, final Action1<T> action1) {
        return new ResponseCallback<T>() {
            @Override
            public void onStart() {
                super.onStart();
                callback.onStart();
            }

            @Override
            public void onSuccess(T t) {
                super.onSuccess(t);
                action1.call(t);
                callback.onSuccess(t);
            }

            @Override
            public void onFailure(Exception e) {
                super.onFailure(e);
                callback.onFailure(e);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callback.onFinish();
            }

            @Override
            public boolean isShowNotice() {
                return callback.isShowNotice();
            }
        };
    }

}
