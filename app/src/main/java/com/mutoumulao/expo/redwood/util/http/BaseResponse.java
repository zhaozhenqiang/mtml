package com.mutoumulao.expo.redwood.util.http;

/**
 * 类描述：
 * 创建人：Bob
 * 创建时间：2016/7/18 15:44
 */
public class BaseResponse {
    // 接口中没有带token信息
    public static final int CODE_TOKEN_EMPTY = 10;
    //token过期
    public static final int CODE_TOKEN_INVALID = 11;
    //在新设备登录, 原设备的session失效(不删除), 使用原token访问api抛出13错误
    // (此时服务端删除原session, 再次用原token访问则抛出12错误)
    public static final int CODE_TOKEN_EXPIRED = 12;
    public static final int CODE_TOKEN_NOT_EXIST = 13;//在其他设备登录，本地令牌失效
    // 客户端时间错误
    public static final int CODE_WRONG_TIME = 16;
    //签名错误，一般是secret与服务端对应不上 由于token失效或者服务端session失效，（用户长时间没有使用app）;
    public static final int CODE_SIGNATURE_ERROR = 21;
    public static String sTimeDiff;
    public int code;
    /**
     * 是否使用加密
     */
    public boolean encode;
    public String msg;

    public String data;

    public String time_diff;

    public boolean isSuccess() {
        return code == 200;
    }

/*    public boolean isTokenExpired() {
        return code == CODE_TOKEN_INVALID ||code == CODE_TOKEN_EXPIRED || code == CODE_TOKEN_NOT_EXIST || code == CODE_SIGNATURE_ERROR;
    }*/

    @Override
    public String toString() {
        return "code:" + code + ",msg:" + msg + ",data:" + data;
    }
}
