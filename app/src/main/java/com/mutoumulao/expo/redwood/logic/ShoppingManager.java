package com.mutoumulao.expo.redwood.logic;

import com.mutoumulao.expo.redwood.base.BaseApp;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.AddressEntity;
import com.mutoumulao.expo.redwood.entity.ShoppingEntity;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.HttpTools;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.util.http.SignRequest;

/**
 * Created by lzy on 2018/8/10.
 * 购物车接口管理
 */

public class ShoppingManager {

    private static ShoppingManager sInstance;
    private final HttpTools mHttpTools;
    private final SharedPutils mPutils;


    public ShoppingManager() {
        mHttpTools = new HttpTools();
        mPutils = SharedPutils.getPreferences(BaseApp.getApp());
    }

    public static ShoppingManager getInstance() {
        if (sInstance == null) {
            synchronized (ShoppingManager.class) {
                if (sInstance == null) {
                    sInstance = new ShoppingManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 购物车列表
     * @param callback
     */
    public void getShopcarList( FinalResponseCallback<ShoppingEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id",  mPutils.getUserID());
        params.setPath(UrlConst.GET_SHOPCAR_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 删除购物车
     * @param callback
     */
    public void getShopCarDel(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", id);
        params.setPath(UrlConst.GET_SHOPCAR_DEL);
        mHttpTools.get(params, callback);
    }

    /**
     * 新加购物车
     * @param callback
     */
    public void getShopCarAdd(String sku_id,String num, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id",  mPutils.getUserID());
        params.addBodyParameter("sku_id", sku_id);
        params.addBodyParameter("num", num);
        params.setPath(UrlConst.POST_SHOPCAR_ADD);
        mHttpTools.post(params, callback);
    }

    /**
     *新增收货地址
     */
    public void addAddress(AddressEntity entity, ResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id",  mPutils.getUserID());
        params.addBodyParameter("name",entity.name);
        params.addBodyParameter("phone",entity.phone);
        params.addBodyParameter("province_code","");
        params.addBodyParameter("province_name",entity.province_name);
        params.addBodyParameter("city_code","");
        params.addBodyParameter("city_name",entity.city_name);
        params.addBodyParameter("area_code","");
        params.addBodyParameter("area_name",entity.area_name);
        params.addBodyParameter("address",entity.address);
        params.addBodyParameter("is_default",entity.is_default);
        params.setPath(UrlConst.POST_ADDRESS_ADD);
        mHttpTools.post(params, callback);
    }

    /**
     *编辑收货地址
     */
    public void updateAddress(AddressEntity entity, String id,ResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addBodyParameter("id",  id);
        params.addBodyParameter("name",entity.name);
        params.addBodyParameter("phone",entity.phone);
        params.addBodyParameter("province_code","");
        params.addBodyParameter("province_name",entity.province_name);
        params.addBodyParameter("city_code","");
        params.addBodyParameter("city_name",entity.city_name);
        params.addBodyParameter("area_code","");
        params.addBodyParameter("area_name",entity.area_name);
        params.addBodyParameter("address",entity.address);
        params.addBodyParameter("is_default",entity.is_default);
        params.setPath(UrlConst.GET_ADDRESS_UPDATE);
        mHttpTools.post(params, callback);
    }

    /**
     *新增收货地址
     */
    public void delAddress(String id, ResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);

        params.setPath(UrlConst.GET_ADDRESS_DEL);
        mHttpTools.post(params, callback);
    }

    /**
     *收货地址列表
     */
    public void addressList( FinalResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id",  mPutils.getUserID());
        params.setPath(UrlConst.GET_ADDRESS_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 查询物流
     */
    public void getQueryShip(String ship_com_code,String ship_order_num, FinalResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("ship_com_code",  ship_com_code);
        params.addQueryStringParameter("ship_order_num",  ship_order_num);
        params.setPath(UrlConst.GET_QUERY_SHIP);
        mHttpTools.get(params, callback);
    }

    /**
     * 确认收货
     */
    public void getReceiveShip(String order_id, ResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("order_id",  order_id);
        params.setPath(UrlConst.GET_RECEIVE_SHIP);
        mHttpTools.get(params, callback);
    }

    /**
     * 发货
     */
    public void getSendShip(String order_id, String ship_com_code,String ship_order_num, ResponseCallback callback){
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("order_id",  order_id);
        params.addQueryStringParameter("ship_com_code",  ship_com_code);
        params.addQueryStringParameter("ship_order_num",  ship_order_num);
        params.setPath(UrlConst.GET_SEND_SHIP);
        mHttpTools.get(params, callback);
    }

    /**
     * 获取快递物流公司
     */
    public void getCompanyShip(FinalResponseCallback callback){
        SignRequest params = new SignRequest();

        params.setPath(UrlConst.GET_COMPANY_SHIP);
        mHttpTools.get(params, callback);
    }
}
