package com.mutoumulao.expo.redwood.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzy on 2018/8/22.
 */

public class OrderEntity implements Serializable{

    /**
     * id : 41
     * author_id : 1
     * business_id : 1
     * order_no : 2018082412351933F0A6FF6EB695236A
     * trade_no : null
     * type : null
     * amount : 0.01
     * status : 0
     * ship_address_id : 1
     * buyer_logon_id : null
     * created_at : 2018-08-24 12:35:19
     * updated_at : null
     * goods_image : ["/upload/18/08/04/10/e8fa2555e277a155fe8eeb1df0e3c4ff.jpg"]
     * goods_name : 鞋子
     * category : 家具
     * category_type : 沙发,非洲黄花梨,光身
     * freight : 0
     * business_name : 御匠红木家具厂
     * business_phone : 15819937879
     * avatar : /avatar/fb4814f8547f991c12ea3aa1257a842ejpg
     * list : [{"id":"49","order_id":"41","goods_id":"11","sku_id":"22","num":"1","goods_serialize_data":null,"sku_info":{"sku_id":"22","spec":"200cm:20m","sku_name":"高度,宽度","price":"0.01","stock":"82"},"goods_name":"鞋子","goods_image":["/upload/18/08/04/10/e8fa2555e277a155fe8eeb1df0e3c4ff.jpg"]}]
     * ship_info : {"id":"1","author_id":"1","name":"测试","phone":"12345678901","province_code":"110000","province_name":"北京","city_code":"110022","city_name":"北京","area_code":"110044","area_name":"昌邑区","address":"颐和园路8弄","is_default":"1","created_at":"2018-08-13 23:08:47","updated_at":"2018-08-16 14:53:25"}
     * pay_order : 20180824123519509B42B1FDD0AFD09F
     */

    public String id;
    public String author_id;
    public String business_id;
    public String order_no;
    public String trade_no;
    public String type;
    public String amount;
    public String status;
    public String ship_address_id;
    public String buyer_logon_id;
    public String pay_time;
    public String created_at;
    public String updated_at;
    public String goods_name;
    public String category;
    public String category_type;
    public String freight;
    public String business_name;
    public String business_phone;
    public String avatar;
    public String pay_typ;
    public String pay_money;
    public ShipInfoEntity ship_info;
    public String pay_order;
    public String daigou_money;
    public List<String> goods_image;
    public List<ListEntity> list;
    public String ship_order_num;
    public String ship_com_code;
    public String auto_receive_time;
    public String goods_price;

    public static class ShipInfoEntity implements Serializable{
        /**
         * id : 1
         * author_id : 1
         * name : 测试
         * phone : 12345678901
         * province_code : 110000
         * province_name : 北京
         * city_code : 110022
         * city_name : 北京
         * area_code : 110044
         * area_name : 昌邑区
         * address : 颐和园路8弄
         * is_default : 1
         * created_at : 2018-08-13 23:08:47
         * updated_at : 2018-08-16 14:53:25
         */
        public String id;
        public String author_id;
        public String name;
        public String phone;
        public String province_code;
        public String province_name;
        public String city_code;
        public String city_name;
        public String area_code;
        public String area_name;
        public String address;
        public String is_default;
        public String created_at;
        public String updated_at;
    }

    public static class ListEntity implements Serializable{
        /**
         * id : 49
         * order_id : 41
         * goods_id : 11
         * sku_id : 22
         * num : 1
         * goods_serialize_data : null
         * sku_info : {"sku_id":"22","spec":"200cm:20m","sku_name":"高度,宽度","price":"0.01","stock":"82"}
         * goods_name : 鞋子
         * goods_image : ["/upload/18/08/04/10/e8fa2555e277a155fe8eeb1df0e3c4ff.jpg"]
         */

        public String id;
        public String order_id;
        public String goods_id;
        public String sku_id;
        public String num;
        public String goods_serialize_data;
        public SkuInfoEntity sku_info;
        public String goods_name;
        public List<String> goods_image;


        public static class SkuInfoEntity implements Serializable{
            /**
             * sku_id : 22
             * spec : 200cm:20m
             * sku_name : 高度,宽度
             * price : 0.01
             * stock : 82
             */

            public String sku_id;
            public String spec;
            public String sku_name;
            public String price;
            public String stock;

        }
    }

}
