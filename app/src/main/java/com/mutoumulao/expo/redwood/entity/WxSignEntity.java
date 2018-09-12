package com.mutoumulao.expo.redwood.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by lzy on 2018/8/29.
 */

public class WxSignEntity {
    /**
     *
     "return_code": "SUCCESS",
     "return_msg": "OK",
     "appid": "wx84bd33fbc16c10b7",
     "mch_id": "1503383261",
     "nonce_str": "lLQOqR0xNGh6inyC",
     "sign": "0BB517F29D8E1A0998762382BF99FDCD",
     "result_code": "SUCCESS",
     "prepay_id": "wx2910521021039900b06e26590362117937",
     "trade_type": "APP",
     "partnerid": "1503383261",
     "timestamp": 1535511130,
     "package": "Sign=WXPay"
     */

    public String return_code;
    public String return_msg;
    public String appid;
    public String mch_id;
    public String nonce_str;
    public String sign;
    public String result_code;
    public String prepay_id;
    public String trade_type;
    public String partnerid;
    public String timestamp;
    @JSONField(name = "package")
    public String packages;


}
