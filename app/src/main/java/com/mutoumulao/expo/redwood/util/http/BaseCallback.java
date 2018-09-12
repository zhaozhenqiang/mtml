package com.mutoumulao.expo.redwood.util.http;

import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mutoumulao.expo.redwood.util.BaseConstant;
import com.mutoumulao.expo.redwood.util.LogUtils;
import com.mutoumulao.expo.redwood.util.RSAUtil;
import com.mutoumulao.expo.redwood.util.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * 类描述：
 * 创建人：Bob
 * 创建时间：2016/7/18 15:35
 */
public abstract class BaseCallback<T> implements Callback<T> {
    public static final String ERR_MSG = "网络连接异常，请稍后再试";


    public void parseData(String str, Handler handler) {
        String _str = RsaDecodeCode(str);

        final BaseResponse response = JSON.parseObject(_str, BaseResponse.class);
//        RsaDeCode(response);//解密

        if (response.isSuccess()) {
            Type genType = getClass().getGenericSuperclass();
            if (!(genType instanceof ParameterizedType)) {
                genType = getClass().getSuperclass().getGenericSuperclass();
            }
            if (!(genType instanceof ParameterizedType)) {
                genType = getClass().getSuperclass().getSuperclass().getGenericSuperclass();
            }
            final Object data;
            Object json;
            try {
                json = JSON.parseObject(response.data, Object.class);//如果data 不是json 解析报错
            } catch (Exception e) {
                json = response.data;

            }
            data = json;

            Class<T> type = null;
            if (genType instanceof ParameterizedType) {
                try {
                    type = ((Class<T>) (((ParameterizedType) (genType)).getActualTypeArguments()[0]));
                } catch (ClassCastException e) {
                }
                // 当responseCallback没有传泛型的时候，直接强转data为泛型类型
                if (type == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess((T) data);
                        }
                    });
                } else {
                    try {
                        if (data instanceof JSONObject) {
                            final T t = JSON.toJavaObject((JSONObject) data, type);
                            if (t == null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onFailure(new Exception(ERR_MSG));
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onSuccess(t);
                                    }
                                });
                            }
                        } else if (data instanceof JSONArray) {
                            final List<T> list = JSON.parseArray(data.toString(), type);
                            if (list == null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onFailure(new Exception(ERR_MSG));
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onSuccess(list);
                                    }
                                });
                            }
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onSuccess((T) data);
                                }
                            });
                        }
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(new Exception(ERR_MSG));
                            }
                        });
                    }
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess((T) data);
                    }
                });
            }
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /*if (response.isTokenExpired()) {
                        EventBus.getDefault().post(new TokenExpiredEvent(response));
                    } else if (response.code == BaseResponse.CODE_TOKEN_EMPTY) {
                        EventBus.getDefault().post(new TokenEmptyEvent(response.data));
                    }
                    if (response.code == BaseResponse.CODE_WRONG_TIME) {
                        BaseResponse.sTimeDiff = response.time_diff;
                    }*/
                    onFailure(new HttpException(response.code, response.msg));
                }
            });
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResponse(response);
            }
        });
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }

    public void onLoading(long total, long current, boolean isUploading) {
    }

    public void onResponse(BaseResponse response) {
//        LogUtils.d("okhttp:response->" + response);
    }

    public void onSuccess(List<T> list) {

    }

    public void onCancelled() {
    }

    private String RsaDecodeCode(String str){
        try{
        if(!TextUtils.isEmpty(str)&& -1!=str.indexOf("\"encode\":true")){
            String codeStrFlay = "\"code\":\"";
            int start = str.indexOf(codeStrFlay)+7;
            String startStr = str.substring(0,start);
            String _str = str.substring(start,str.length());
            int codeEndIndex = _str.indexOf("\",\"");
            String codeStr = _str.substring(1,codeEndIndex);
            String endStr = _str.substring(codeEndIndex+1, _str.length());
            String code = RSAUtil.decryptByPrivateKey(codeStr, BaseConstant.KEY_RESPONSE);
            LogUtils.d("okhttp:RsaDeCode-> code:" + code);
            str = startStr+code+endStr;
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
    private BaseResponse RsaDeCode(BaseResponse response) {
        if (response.encode) {//需要解密
            if (response.data != null) {
                try {
                    String data = RSAUtil.decryptByPrivateKey(response.data, BaseConstant.KEY_RESPONSE);
                    if (!TextUtils.isEmpty(data) ) {
//                        String unicode = StringUtil.decodeUnicode(data);
                        response.data = data;
                        data = StringUtil.formatJson(data);
                        LogUtils.d("okhttp:RsaDeCode-> data:" + data);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        return response;
    }

}
