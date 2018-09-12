package com.mutoumulao.expo.redwood.logic;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mutoumulao.expo.redwood.base.BaseApp;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.LoginEntity;
import com.mutoumulao.expo.redwood.entity.event.AccountChangeEvent;
import com.mutoumulao.expo.redwood.hx.DemoHelper;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.http.HttpTools;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.util.http.SignRequest;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lzy on 2018/8/20.
 */

public class LoginManager {
    private static LoginManager sInstance;
    private final HttpTools mHttpTools;
    private final SharedPutils mPutils;

    private LoginManager() {
        mHttpTools = new HttpTools();
        mPutils = SharedPutils.getPreferences(BaseApp.getApp());
    }

    public static LoginManager getInstance() {
        if (sInstance == null) {
            synchronized (LoginManager.class) {
                if (sInstance == null) {
                    sInstance = new LoginManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 第三方登录
     *
     * @param openid
     * @param type     (qq,weixin,weibo)
     * @param callback
     */
    public void getOtherLogin(String openid, String type, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("openid", openid);
        params.addQueryStringParameter("type", type);
        params.setPath(UrlConst.GET_OTHER_LOGIN);
        mHttpTools.get(params, new LoginCallback(callback));
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param type     1:登陆 2:注册 3:修改密码
     * @param callback
     */
    public void getSendMessage(String phone, String type, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("type", type);
        params.setPath(UrlConst.GET_SEND_MESSAGE);
        mHttpTools.get(params, callback);
    }

    /**
     * 手机号注册
     *
     * @param phone
     * @param password
     * @param code
     * @param callback
     */
    public void getRegister(String phone, String password, String code, ResponseCallback<LoginEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("code", code);
        params.setPath(UrlConst.GET_REGISTER);
        mHttpTools.get(params, new LoginCallback(callback));
    }


    /**
     * 账号密码登录
     *
     * @param phone
     * @param password
     * @param callback
     */
    public void getLogin(String phone, String password, ResponseCallback<LoginEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("password", password);
        params.setPath(UrlConst.GET_LOGIN);
        mHttpTools.get(params, new LoginCallback(callback));
    }


    /**
     * 手机号登陆
     *
     * @param phone
     * @param code
     * @param callback
     */
    public void getMessageLogin(String phone, String code, ResponseCallback<LoginEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("code", code);
        params.setPath(UrlConst.GET_MESSAGE_LOGIN);
        mHttpTools.get(params, new LoginCallback(callback));
    }

    /**
     * 忘记密码
     *
     * @param phone
     * @param code
     * @param password 新密码
     * @param callback
     */
    public void getForgetLogin(String phone, String password, String code, ResponseCallback<LoginEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("code", code);
        params.setPath(UrlConst.GET_FORGET_PASS);
        mHttpTools.get(params, callback);
    }

    /**
     * 手机号绑定
     *
     * @param phone
     * @param code
     * @param openid
     * @param type     qq,weixin,weibo
     * @param callback
     */
    public void getUserBind(String phone, String openid, String code, String type, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("openid", openid);
        params.addQueryStringParameter("code", code);
        params.addQueryStringParameter("type", type);
        params.setPath(UrlConst.GET_USER_BIND);
        mHttpTools.get(params, new LoginCallback(callback));
    }

    /**
     * 获取个人信息
     *
     * @param callback
     */
    public void getUserInfo(ResponseCallback<LoginEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.setPath(UrlConst.GET_USER_INFO);
        mHttpTools.get(params, new getUserCallback(callback));
    }

    /**
     * 修改个人信息
     *
     * @param callback
     */
    public void updataUserInfo(String nickname, String avatar, ResponseCallback<LoginEntity> callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("avatar", avatar);
        params.setPath(UrlConst.POST_USER_UPDATA);
        mHttpTools.post(params, callback);
    }


    private class LoginCallback extends ResponseCallback<LoginEntity> {
        private ResponseCallback<LoginEntity> callBack;

        public LoginCallback(ResponseCallback<LoginEntity> callBack) {
            this.callBack = callBack;
        }


        @Override
        public void onStart() {
            super.onStart();
            callBack.onStart();
        }

        @Override
        public void onSuccess(final LoginEntity info) {
            super.onSuccess(info);
            try {
                if (info != null) {
                    mPutils.setUserID(info.author_id);
                    mPutils.setAvatar(info.avatar);
                    mPutils.setPhone(info.phone);
                    mPutils.setUserName(info.nickname);
                    mPutils.setIs_business(info.is_business);
                    if (info.business != null) {
                        mPutils.setBusinessId(info.business.id);
                        mPutils.setIsClose(info.business.is_close);//1 是打开
                        mPutils.setState(info.business.status);//是0  说明审核不通过
                        mPutils.setIsCheck(info.business.is_check);//0:待审核，1:已通过，2::未通过
                    }
                    EMClient.getInstance().login(info.phone, "111111", new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            try {
                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();

                                EventBus.getDefault().post(new AccountChangeEvent(true));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            String s1 = s;
                            try {
                                EMClient.getInstance().createAccount(info.phone, "111111");
                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                                DemoHelper.getInstance().setCurrentUserName(info.phone);
                                EventBus.getDefault().post(new AccountChangeEvent(true));
                                EMClient.getInstance().login(info.phone, "111111", new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        try {
                                            EMClient.getInstance().chatManager().loadAllConversations();
                                            EMClient.getInstance().groupManager().loadAllGroups();

                                            EventBus.getDefault().post(new AccountChangeEvent(true));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        String s1 = s;


                                    }

                                    @Override
                                    public void onProgress(int i, String s) {

                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.USER_ALREADY_EXIST) {
                                    EMClient.getInstance().login(info.phone, "111111", new EMCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            try {
                                                EMClient.getInstance().chatManager().loadAllConversations();
                                                EMClient.getInstance().groupManager().loadAllGroups();

                                                EventBus.getDefault().post(new AccountChangeEvent(true));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            String s1 = s;


                                        }

                                        @Override
                                        public void onProgress(int i, String s) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Exception error) {
            super.onFailure(error);
            callBack.onFailure(error);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            callBack.onFinish();
        }

        @Override
        public boolean isShowNotice() {
            return callBack.isShowNotice();
        }
    }


    private class getUserCallback extends ResponseCallback<LoginEntity> {
        private ResponseCallback<LoginEntity> callBack;

        public getUserCallback(ResponseCallback<LoginEntity> callBack) {
            this.callBack = callBack;
        }


        @Override
        public void onStart() {
            super.onStart();
            callBack.onStart();
        }

        @Override
        public void onSuccess(LoginEntity info) {
            super.onSuccess(info);
            try {
                if (info != null) {
                    mPutils.setUserID(info.author_id);
                    mPutils.setAvatar(info.avatar);
                    mPutils.setPhone(info.phone);
                    mPutils.setUserName(info.nickname);
                    mPutils.setIs_business(info.is_business);
                    if (info.business != null) {
                        mPutils.setBusinessId(info.business.id);
                        mPutils.setIsClose(info.business.is_close);//1 是打开
                        mPutils.setState(info.business.status);//是0  说明审核不通过
                        mPutils.setIsCheck(info.business.is_check);//0:待审核，1:已通过，2::未通过

                    }

                }
                callBack.onSuccess(info);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Exception error) {
            super.onFailure(error);
            callBack.onFailure(error);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            callBack.onFinish();
        }

        @Override
        public boolean isShowNotice() {
            return callBack.isShowNotice();
        }
    }


}
