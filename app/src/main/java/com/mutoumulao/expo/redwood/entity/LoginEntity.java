package com.mutoumulao.expo.redwood.entity;

/**
 * Created by lzy on 2018/8/21.
 */

public class LoginEntity {

    /**
     * author_id : 107
     * nickname : 17721111165
     * avatar : /avatar/default.jpg
     * is_business : 1
     * phone : 17721111165
     * business : {"id":"38","author_id":"107","business_name":"tencent","avatar":"/upload/18/08/20/13/3a4f60c8d8c8bc396cf346bb31d64096.jpg","phone":"17721111165","province_code":"","province_name":"内蒙古","city_code":"","city_name":"兴安盟","area_code":"","area_name":"突泉县","address":"xaingxidizhi","description":"jieshaoxiangqing","business_license":"/upload/18/08/20/13/2c51e77cd1b1393dd6ea092bf11db7a3.jpg","sale_num":"0","created_at":"2018-08-20 13:28:29","updated_at":null,"is_check":"0","status":"0"}
     */

    public String author_id;
    public String nickname;
    public String avatar;
    public String is_business;//1代表绑定商家 ，0表示非商家
    public String phone;
    public BusinessEntity business;


    public static class BusinessEntity {
        /**
         * id : 38
         * author_id : 107
         * business_name : tencent
         * avatar : /upload/18/08/20/13/3a4f60c8d8c8bc396cf346bb31d64096.jpg
         * phone : 17721111165
         * province_code :
         * province_name : 内蒙古
         * city_code :
         * city_name : 兴安盟
         * area_code :
         * area_name : 突泉县
         * address : xaingxidizhi
         * description : jieshaoxiangqing
         * business_license : /upload/18/08/20/13/2c51e77cd1b1393dd6ea092bf11db7a3.jpg
         * sale_num : 0
         * created_at : 2018-08-20 13:28:29
         * updated_at : null
         * is_check : 0
         * status : 0
         * is_close : 1
         */

        public String id;
        public String author_id;
        public String business_name;
        public String avatar;
        public String phone;
        public String province_code;
        public String province_name;
        public String city_code;
        public String city_name;
        public String area_code;
        public String area_name;
        public String address;
        public String description;
        public String business_license;
        public String sale_num;
        public String created_at;
        public Object updated_at;
        public String is_check;
        public String is_close;
        public String status;


    }
}
