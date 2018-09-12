package com.mutoumulao.expo.redwood.logic;

import android.text.TextUtils;

import com.mutoumulao.expo.redwood.base.BaseApp;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.GoodsDetialEntity;
import com.mutoumulao.expo.redwood.entity.PositionEntity;
import com.mutoumulao.expo.redwood.entity.StoreEntity;
import com.mutoumulao.expo.redwood.entity.StoreManagerListEntity;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.HttpTools;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.util.http.SignRequest;

import java.io.File;

/**
 * Created by lzy on 2018/8/2.
 * <p>
 * 我的界面接口功能管理类
 */

public class MineFuncManager {

    private static MineFuncManager sInstance;
    private final HttpTools mHttpTools;
    private final SharedPutils mPutils;

    public MineFuncManager() {
        mHttpTools = new HttpTools();
        mPutils = SharedPutils.getPreferences(BaseApp.getApp());
    }

    public static MineFuncManager getInstance() {
        if (sInstance == null) {
            synchronized (MineFuncManager.class) {
                if (sInstance == null) {
                    sInstance = new MineFuncManager();
                }
            }
        }
        return sInstance;
    }

    /*------------------------职位管理--------------------------*/

    /**
     * 职位列表
     *
     * @param page
     * @param callback
     */
    public void getRecruitmentList(int page, String keyword, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        params.setPath(UrlConst.GET_POSITION_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 职位列表 首页yoga
     *
     * @param page
     * @param callback
     */
    public void getRecruitmentList2(int page, String keyword, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
//        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        params.setPath(UrlConst.GET_POSITION_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 商家用
     * @param page
     * @param author_id
     * @param callback
     */
    public void getRecruitmentList1(int page, String author_id, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", author_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", "");
        params.setPath(UrlConst.GET_POSITION_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 删除职位
     *
     * @param id
     * @param callback
     */
    public void deleteRecruitment(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.setPath(UrlConst.POST_POSITION_DEL);
        mHttpTools.post(params, callback);
    }

    /**
     * 发布职位
     *
     * @param title
     * @param content
     * @param images
     * @param callback
     */
    public void publishRecruitment(String title, String content, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_POSITION_ADD);
        mHttpTools.post(params, callback);
    }

    /**
     * 更新职位
     *
     * @param title
     * @param content
     * @param images
     * @param callback
     */
    public void updataRecruitment(String id, String title, String content, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_POSITION_UPDATE);
        mHttpTools.post(params, callback);
    }

       /*------------------------图片管理--------------------------*/

    /**
     * 图片上传
     *
     * @param photo
     * @param callback
     */
    public void sendPhoto(File photo, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("photo", photo, photo.getName(), "image/png");
        params.setPath(UrlConst.POST_IMAGE_UPLOAD);
        mHttpTools.post(params, callback);
    }


    /**
     * 图片上传
     *
     * @param content
     * @param callback
     */
    public void sendMessage(String content,String email,String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("content", content);
        params.addBodyParameter("email", email);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_FEEDBACK_ADD);
        mHttpTools.post(params, callback);
    }


    /*------------------------服务管理--------------------------*/

    /**
     * 发布服务
     *
     * @param title
     * @param content
     * @param images
     * @param callback
     */
    public void publishService(String title, String content, String category_id, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);
        params.addBodyParameter("category_id", category_id);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_SERVICE_ADD);
        mHttpTools.post(params, callback);
    }

    /**
     * 发布服务
     *
     * @param title
     * @param content
     * @param images
     * @param callback
     */
    public void updataService(String id, String title, String content, String category_id, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);
        params.addBodyParameter("category_id", category_id);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_SERVICE_UPDATE);
        mHttpTools.post(params, callback);
    }

    /**
     * 服务分类
     *
     * @param callback
     */
    public void serviceCategory(ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.setPath(UrlConst.POST_SERVICE_CATEGORY);
        mHttpTools.post(params, callback);
    }

    /**
     * 服务列表
     *
     * @param page
     * @param callback
     */
    public void getServiceList(int page, String keyword, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        params.setPath(UrlConst.GET_SERVICE_LIST);
        mHttpTools.get(params, callback);
    }

    //首页用
    public void getServiceList(int page, String keyword, String category_name, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
/*        params.addQueryStringParameter("author_id", mPutils.getUserID());*/
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        if (!TextUtils.isEmpty(category_name)) {
            params.addQueryStringParameter("category_name", category_name);
        }
        params.setPath(UrlConst.GET_SERVICE_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 删除服务
     *
     * @param id
     * @param callback
     */
    public void deleteService(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.setPath(UrlConst.POST_SERVICE_DEL);
        mHttpTools.post(params, callback);
    }

    /*------------------------求职管理--------------------------*/

    /**
     * 发布求职
     *
     * @param title
     * @param content
     * @param images
     * @param callback
     */
    public void publishJob(String title, String content, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_JOB_ADD);
        mHttpTools.post(params, callback);
    }

    /**
     * 更新职位
     *
     * @param title
     * @param content
     * @param images
     * @param callback
     */
    public void updataJob(String id, String title, String content, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_JOB_UPDATE);
        mHttpTools.post(params, callback);
    }


    /**
     * 求职列表
     *
     * @param page
     * @param callback
     */
    public void getJobList(int page, String keyword, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        params.setPath(UrlConst.GET_JOB_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 求职列表 首页用
     *
     * @param page
     * @param callback
     */
    public void getJobList1(int page, String keyword, FinalResponseCallback<PositionEntity> callback) {
        SignRequest params = new SignRequest();
//        params.addQueryStringParameter("author_id", author_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        params.setPath(UrlConst.GET_JOB_LIST);
        mHttpTools.get(params, callback);
    }


    /**
     * 删除求职
     *
     * @param id
     * @param callback
     */
    public void deleteJob(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.setPath(UrlConst.POST_JOB_DEL);
        mHttpTools.post(params, callback);
    }

    /*------------------------商品管理--------------------------*/

    /**
     * 商品列表
     *
     * @param page
     * @param callback
     */
    public void getGoodsList(int page, FinalResponseCallback<StoreManagerListEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
 /*       params.addQueryStringParameter("category", "0");*/
        params.setPath(UrlConst.GET_GOODS_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 商品列表 家详情用
     *
     * @param page
     * @param callback
     */
    public void getGoodsList(int page, String author_id, FinalResponseCallback<StoreManagerListEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", author_id);
        params.addQueryStringParameter("page", page + "");
/*        params.addQueryStringParameter("category", "0");*/
        params.setPath(UrlConst.GET_GOODS_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 商品列表(首页用)
     *
     * @param page
     * @param category
     * @param type     sale_num ,price ,is_new
     * @param sort     1升序 2降序
     * @param callback
     */
    public void getGoodsList(int page, String author_id, String category, String type, String sort, String key_word, FinalResponseCallback<StoreManagerListEntity> callback) {
        SignRequest params = new SignRequest();
        if (!TextUtils.isEmpty(author_id)) {
            params.addQueryStringParameter("author_id", author_id);
        }
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("category", category);
        params.addQueryStringParameter(type, sort);
        params.addQueryStringParameter("keyword", key_word);
        params.setPath(UrlConst.GET_GOODS_LIST);

        mHttpTools.get(params, callback);
    }


    /**
     * 商家列表(首页用)
     *
     * @param page
     * @param keyword
     * @param type     sale_num
     * @param sort     1升序 2降序
     * @param callback
     */
    public void getStoreList(int page, String keyword, String type, String sort, FinalResponseCallback<StoreEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("keyword", keyword);
        params.addQueryStringParameter(type, sort);
        params.setPath(UrlConst.GET_BUSINESS_LIST);

        mHttpTools.get(params, callback);
    }

    public void getGoodDetial(String id, ResponseCallback<GoodsDetialEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("id", id);
        params.setPath(UrlConst.GET_GOODS_DETAIL);
        mHttpTools.get(params, callback);

    }

    /**
     * 获取商家详情
     *
     * @param callback
     */
    public void geStoreDetial(ResponseCallback<StoreEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", mPutils.getBusinessId());
        params.setPath(UrlConst.GET_BUSINESS_DETAIL);
        mHttpTools.get(params, callback);
    }

    /**
     * 获取商家详情(首页用)
     *
     * @param callback
     */
    public void geStoreDetial(String id, ResponseCallback<StoreEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", id);
        params.setPath(UrlConst.GET_BUSINESS_DETAIL);
        mHttpTools.get(params, callback);
    }

    /**
     * 暂停营业
     *
     * @param callback
     */
    public void getStoreClose(String state,ResponseCallback<StoreEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", mPutils.getBusinessId());
        params.addQueryStringParameter("is_close", state);
        params.setPath(UrlConst.GET_BUSINESS_CLOSE);
        mHttpTools.get(params, callback);
    }

    /**
     * 商家认证
     *
     * @param
     */
    public void geStoreAuth(String business_name, String avatar, String phone, String province_name,
                            String city_name, String area_name, String address, String description,
                            String business_license, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("business_name", business_name);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("province_code", "");
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city_code", "");
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("area_code", "");
        params.addBodyParameter("area_name", area_name);
        params.addBodyParameter("address", address);
        params.addBodyParameter("description", description);
        params.addBodyParameter("business_license", business_license);
        params.setPath(UrlConst.POST_BUSINESS_ADD);
        mHttpTools.post(params, callback);
    }


    /**
     * 商家重新认证
     *
     * @param
     */
    public void geStoreReAuth(String id, String business_name, String avatar, String phone, String province_name,
                            String city_name, String area_name, String address, String description,
                            String business_license, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.addBodyParameter("business_name", business_name);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("province_code", "");
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city_code", "");
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("area_code", "");
        params.addBodyParameter("area_name", area_name);
        params.addBodyParameter("address", address);
        params.addBodyParameter("description", description);
        params.addBodyParameter("business_license", business_license);
        params.setPath(UrlConst.POST_BUSINESS_RE_AUTH);
        mHttpTools.post(params, callback);
    }

    /**
     * 商家编辑
     *
     * @param
     */
    public void storeupdate(String id, String business_name, String avatar, String phone, String province_name,
                            String city_name, String area_name, String address, String description,
                            String business_license, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.addBodyParameter("business_name", business_name);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("province_code", "");
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city_code", "");
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("area_code", "");
        params.addBodyParameter("area_name", area_name);
        params.addBodyParameter("address", address);
        params.addBodyParameter("description", description);
        params.addBodyParameter("business_license", business_license);
        params.setPath(UrlConst.POST_BUSINESS_UPDATE);
        mHttpTools.post(params, callback);
    }


    /**
     * 新增商品
     *
     * @param goods_name
     * @param goods_image
     * @param category
     * @param category_type
     * @param freight
     * @param desc
     * @param desc_image
     * @param send_address
     * @param list
     * @param callback
     */
    public void sendGoodsAdd(String goods_name, String goods_image, String category,
                             String category_type, String freight, String desc,
                             String desc_image, String send_address, String list,
                             ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id", mPutils.getUserID());
        params.addBodyParameter("goods_name", goods_name);
        params.addBodyParameter("goods_image", goods_image);
        params.addBodyParameter("category", category);
        params.addBodyParameter("category_type", category_type);
        params.addBodyParameter("freight", freight);
        params.addBodyParameter("desc", desc);
        params.addBodyParameter("desc_image", desc_image);
        params.addBodyParameter("send_address", send_address);
        params.addBodyParameter("list", list);
        params.setPath(UrlConst.POST_GOODS_ADD);
        mHttpTools.post(params, callback);

    }

    /**
     * 更新商品
     *
     * @param goods_name
     * @param goods_image
     * @param category
     * @param category_type
     * @param freight
     * @param desc
     * @param desc_image
     * @param send_address
     * @param list
     * @param callback
     */
    public void sendGoodsUpdate(String id, String goods_name, String goods_image, String category,
                                String category_type, String freight, String desc,
                                String desc_image, String send_address, String list,
                                ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("id", id);
        params.addBodyParameter("goods_name", goods_name);
        params.addBodyParameter("goods_image", goods_image);
        params.addBodyParameter("category", category);
        params.addBodyParameter("category_type", category_type);
        params.addBodyParameter("freight", freight);
        params.addBodyParameter("desc", desc);
        params.addBodyParameter("desc_image", desc_image);
        params.addBodyParameter("send_address", send_address);
        params.addBodyParameter("list", list);
        params.setPath(UrlConst.POST_GOODS_UPDATE);
        mHttpTools.post(params, callback);

    }

    /**
     * 商品删除
     *
     * @param callback
     */
    public void goodsDel(String id, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("id", id);
        params.setPath(UrlConst.GET_GOODS_DEL);
        mHttpTools.get(params, callback);

    }

    /**
     * 我的足迹
     *
     * @param page
     * @param callback
     */
    public void getBrowseHistory(int page, FinalResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.setPath(UrlConst.GET_BROWSE_HISTORY);
        mHttpTools.post(params, callback);

    }

    /**
     * 我的足迹
     *
     * @param page
     * @param callback
     */
    public void getCommentList(int page, String goods_id, FinalResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("author_id", mPutils.getUserID());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("goods_id", goods_id);
        params.setPath(UrlConst.GET_COMMENT_LIST);
        mHttpTools.post(params, callback);

    }


}
