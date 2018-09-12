package com.mutoumulao.expo.redwood.logic;

import com.mutoumulao.expo.redwood.base.BaseApp;
import com.mutoumulao.expo.redwood.constants.UrlConst;
import com.mutoumulao.expo.redwood.entity.HomeListEntity;
import com.mutoumulao.expo.redwood.util.SharedPutils;
import com.mutoumulao.expo.redwood.util.http.FinalResponseCallback;
import com.mutoumulao.expo.redwood.util.http.HttpTools;
import com.mutoumulao.expo.redwood.util.http.ResponseCallback;
import com.mutoumulao.expo.redwood.util.http.SignRequest;

/**
 * Created by lzy on 2018/8/10.
 */

public class HomeManager {

    private static HomeManager sInstance;
    private final HttpTools mHttpTools;
    private final SharedPutils mPutils;

    public HomeManager() {
        mHttpTools = new HttpTools();
        mPutils = SharedPutils.getPreferences(BaseApp.getApp());
    }

    public static HomeManager getInstance() {
        if (sInstance == null) {
            synchronized (HomeManager.class) {
                if (sInstance == null) {
                    sInstance = new HomeManager();
                }
            }
        }
        return sInstance;
    }

    /*------------------------文章管理--------------------------*/

    /**
     * 文章列表
     *
     * @param page
     * @param callback
     */
    public void getArticleList(int page, String category, FinalResponseCallback<HomeListEntity> callback) {
        SignRequest params = new SignRequest();
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("category", category);
        params.setPath(UrlConst.GET_ARTICLE_LIST);
        mHttpTools.get(params, callback);
    }

    /**
     * 增加评论
     * @param article_id
     * @param content
     * @param images
     * @param callback
     */
    public void SendComment(String article_id, String content, String images, ResponseCallback callback) {
        SignRequest params = new SignRequest();
        params.addBodyParameter("author_id",  mPutils.getUserID());
        params.addBodyParameter("article_id", article_id);
        params.addBodyParameter("content", content);
        params.addBodyParameter("images", images);
        params.setPath(UrlConst.POST_ARTICLE_COMMENT);
        mHttpTools.post(params, callback);
    }


}
