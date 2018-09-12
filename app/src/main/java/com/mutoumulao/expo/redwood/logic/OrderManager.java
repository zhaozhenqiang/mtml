package com.mutoumulao.expo.redwood.logic;

import com.mutoumulao.expo.redwood.base.BaseApp;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.OrderEntity;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.HttpTools;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.util.http.SignRequest;

/**
 * Created by lzy on 2018/8/22.
 */

public class OrderManager {

    private static OrderManager sInstance;
    private final HttpTools mHttpTools;
    private final SharedPutils mPutils;


    public OrderManager() {
        mHttpTools = new HttpTools();
        mPutils = SharedPutils.getPreferences(BaseApp.getApp());
    }

    public static OrderManager getInstance() {
        if (sInstance == null) {
            synchronized (OrderManager.class) {
                if (sInstance == null) {
                    sInstance = new OrderManager();
                }
            }
        }
        return sInstance;
    }


    /**
     * 订单列表
     *
     * @param page
     * @param status
     * @param callback
     */
    public void getOrderList(int page, int status, FinalResponseCallback<OrderEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id",  mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("status", status + "");
        params.setPath(UrlConst.GET_ORDER_LIST);
        mHttpTools.get(params, callback);
    }
    /**
     * 订单列表商家
     *
     * @param page
     * @param status
     * @param callback
     */
    public void getOrderBuinessList(int page, int status, FinalResponseCallback<OrderEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id",  mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("status", status + "");
        params.setPath(UrlConst.GET_ORDER_BUSINESSLIST);
        mHttpTools.get(params, callback);
    }


    /**
     * 立即购买
     *
     * @param sku_id
     * @param num
     * @param is_daigou 是否代购(0:否，1:是)
     * @param ship_address_id
     * @param callback
     */
    public void immediatelyBuy(String sku_id, String num, String is_daigou, String ship_address_id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id",  mPutils.getUserID());
        params.addBodyParameter("sku_id", sku_id);
        params.addBodyParameter("num", num);
        params.addBodyParameter("is_daigou", is_daigou);
        params.addBodyParameter("ship_address_id", ship_address_id);
        params.setPath(UrlConst.POST_ORDER_BUY);
        mHttpTools.post(params, callback);
    }

    /**
     * 购物车结算
     *
     * @param item
     * @param is_daigou 是否代购(0:否，1:是)
     * @param ship_address_id
     * @param callback
     */
    public void shoppongCarBuy(String item,  String is_daigou, String ship_address_id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id",  mPutils.getUserID());
        params.addBodyParameter("item", item);
        params.addBodyParameter("is_daigou", is_daigou);
        params.addBodyParameter("ship_address_id", ship_address_id);
        params.setPath(UrlConst.POST_ORDER_CARTBUY);
        mHttpTools.post(params, callback);
    }


    /**
     * 取消订单
     *
     * @param id
     * @param callback
     */
    public void getOrderCancel(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", id);
        params.setPath(UrlConst.GET_ORDER_CANCEL_ORDER);
        mHttpTools.get(params, callback);
    }


    /**
     * 订单详情
     *
     * @param id
     * @param callback
     */
    public void getOrderDetial(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", id);
        params.setPath(UrlConst.GET_ORDER_DETIAl);
        mHttpTools.get(params, callback);
    }

    /**
     * 微信获取sign
     *
     * @param pay_order
     * @param callback
     */
    public void getWxSign(String pay_order, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("pay_order", pay_order);
        params.setPath(UrlConst.GET_WX_SIGN);
        mHttpTools.get(params, callback);
    }


    /**
     * 订单评论
     * @param order_id
     * @param content
     * @param desc_star
     * @param wuliu_star
     * @param fuwu_star
     * @param images
     * @param callback
     */
    public void commentOrder(String order_id,  String content, String desc_star,
                             String wuliu_star, String fuwu_star,String images,
                             ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id",  mPutils.getUserID());
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("content", content);
        params.addBodyParameter("desc_star", desc_star);
        params.addBodyParameter("wuliu_star", wuliu_star);
        params.addBodyParameter("fuwu_star", fuwu_star);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_ORDER_COMMENT);
        mHttpTools.post(params, callback);
    }

}
