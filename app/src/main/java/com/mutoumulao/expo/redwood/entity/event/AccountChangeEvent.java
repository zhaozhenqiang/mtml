package com.mutoumulao.expo.redwood.entity.event;

/**
 * 类描述：
 * 创建人：chenbo
 * 创建时间：2015/6/29 10:23
 */
public class AccountChangeEvent {
    public boolean isLogin;

    public AccountChangeEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
