package com.mutoumulao.expo.redwood.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzy on 2018/8/8.
 */

public class StoreManagerListEntity implements Serializable {
    /*
    "id": "8",
    "author_id": "1",
    "goods_name": "韭菜盒子",
    "category": "家具",
    "category_type": "桌类,非洲黄花梨,光身",
    "desc": "商品描述",
    "desc_image": "/upload/18/08/04/08/7d8e7f094a551562d5dfe30bffd512eb.jpg;/upload/18/08/04/08/56cc0d95cafd0828c679983f6381f9e3.jpg",
    "goods_image": "/upload/18/08/04/08/7d8e7f094a551562d5dfe30bffd512eb.jpg;/upload/18/08/04/08/56cc0d95cafd0828c679983f6381f9e3.jpg",
    "freight": "998",
    "send_address": "江苏省,泰州市,高港区",
    "comment_num": "0",
    "created_at": "2018-08-04 08:11:20",
    "updated_at": "2018-08-04 08:11:20",
    "nickname": null,
    "list": [{
        "spec": "绿色:10寸",
        "sku_name": "颜色",
        "price": "10.00",
        "stock": "20"
    }, {
        "spec": "红色:10寸",
        "sku_name": "颜色",
        "price": "30.00",
        "stock": "40"
    }, {
        "spec": "绿色:20寸",
        "sku_name": "颜色",
        "price": "50.00",
        "stock": "60"
    }, {
        "spec": "红色:20寸",
        "sku_name": "颜色",
        "price": "80.00",
        "stock": "90"
    }
     */
    public String id;
    public String author_id;
    public String goods_name;
    public String category;
    public String category_type;
    public String desc;
    public String price;
    public List<String> desc_image;
    public List<String> goods_image;
    public String freight;
    public String send_address;
    public String comment_num;
    public String sku_name;
    public String created_at;
    public String updated_at;
    public String business_name;
    public String sale_num;
    public String minPrice;
    public String maxPrice;
    public List<SkuListEntity> skuList;
    public List<GuigesEntity> guiges;

    public static class SkuListEntity implements Serializable {
        /*
        "sku_id": "7",
		"spec": "绿色:10寸",
		"sku_name": "颜色,尺码",
		"price": "10.00",
		"stock": "0"
         */
        public String sku_id;
        public String spec;
        public String sku_name;
        public String price;
        public String stock;
    }

    public static class GuigesEntity implements Serializable {
        /*
        "title": "颜色",
		"guigeArray": ["绿色", "红色"]
         */
        public boolean selfFlag;
        public String title;
        //public List<String> guigeArray= new ArrayList<>();
    }

}
