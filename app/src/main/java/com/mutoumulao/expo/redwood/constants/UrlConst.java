package com.mutoumulao.expo.redwood.constants;

import com.mutoumulao.expo.redwood.BuildConfig;

/**
 * Created by lzy on 2018/8/2.
 */

public class UrlConst {
    public static final String BASE_URL = BuildConfig.SERVER_IP;
    public static final String IMAGE_URL = BuildConfig.IMAGE_IP;
    public static final String RSA_PRIVATE = BuildConfig.RSA_PRIVATE;

    // 支付宝回调
    public static final String PAY_URL = IMAGE_URL + "/pay/ali-notify";// 支付宝回调
    /* ----------------  职位管理 ------------------*/
    public static final String GET_POSITION_LIST = BASE_URL + "/position/list";//职位列表
    public static final String POST_POSITION_DEL = BASE_URL + "/position/del";//删除职位
    public static final String POST_POSITION_DELIMAGE = BASE_URL + "/position/del-image";//图片删除
    public static final String POST_POSITION_UPDATE = BASE_URL + "/position/update";//更新职位
    public static final String POST_POSITION_ADD = BASE_URL + "/position/add";//发布职位
    public static final String POST_FEEDBACK_ADD = BASE_URL + "/feedback/add";//意见反馈

    /* ----------------  服务管理 ------------------*/
    public static final String GET_SERVICE_LIST = BASE_URL + "/service/list";//服务列表
    public static final String POST_SERVICE_CATEGORY = BASE_URL + "/service/category";//服务分类
    public static final String POST_SERVICE_DEL = BASE_URL + "/service/del";//服务删除
    public static final String POST_SERVICE_DELIMAGE = BASE_URL + "/service/del-image";//图片删除
    public static final String POST_SERVICE_UPDATE = BASE_URL + "/service/update";//服务编辑
    public static final String POST_SERVICE_ADD = BASE_URL + "/service/add";//发布服务接口

    /* ----------------  求职管理 ------------------*/
    public static final String GET_JOB_LIST = BASE_URL + "/job/list";//job列表
    public static final String POST_JOB_CATEGORY = BASE_URL + "/job/category";//job分类
    public static final String POST_JOB_DEL = BASE_URL + "/job/del";//job删除
    public static final String POST_JOB_DELIMAGE = BASE_URL + "/job/del-image";//图片删除
    public static final String POST_JOB_UPDATE = BASE_URL + "/job/update";//job编辑
    public static final String POST_JOB_ADD = BASE_URL + "/job/add";//发布job接口


    /* ----------------  图片上传 ------------------*/
    public static final String POST_IMAGE_UPLOAD = BASE_URL + "/image/upload";//图片上传

    /* ----------------  商品管理 ------------------*/
    public static final String GET_GOODS_LIST = BASE_URL + "/goods/list";//商品列表
    public static final String GET_GOODS_DETAIL = BASE_URL + "/goods/detail";//商品详情
    public static final String POST_GOODS_ADD = BASE_URL + "/goods/add";//新增商品
    public static final String POST_GOODS_UPDATE = BASE_URL + "/goods/update";//新增商品
    public static final String GET_GOODS_DEL = BASE_URL + "/goods/del";//商品删除
    public static final String GET_BROWSE_HISTORY = BASE_URL + "/goods/browse-history";//我的足迹
    public static final String GET_COMMENT_LIST = BASE_URL + "/goods/comment-list";//评论列表

    /* ----------------  文章管理 ------------------*/
    public static final String GET_ARTICLE_LIST = BASE_URL + "/article/list";//文章列表
    public static final String POST_ARTICLE_COMMENT = BASE_URL + "/article/comment";//新增品论


    /* ----------------  购物车管理 ------------------*/
    public static final String GET_SHOPCAR_LIST = BASE_URL + "/shop-cart/list";//购物车列表
    public static final String GET_SHOPCAR_DEL = BASE_URL + "/shop-cart/del";//删除购物车
    public static final String POST_SHOPCAR_ADD = BASE_URL + "/shop-cart/add";//新增购物车
    public static final String POST_ADDRESS_ADD = BASE_URL + "/ship-address/add";//增加收货地址
    public static final String GET_ADDRESS_DEL = BASE_URL + "/ship-address/del";//删除收货地址
    public static final String GET_ADDRESS_UPDATE = BASE_URL + "/ship-address/update";//删除收货地址
    public static final String GET_ADDRESS_LIST = BASE_URL + "/ship-address/list";//收货地址列表
    public static final String GET_QUERY_SHIP = BASE_URL + "/ship-address/query-ship";//查询物流
    public static final String GET_COMPANY_SHIP = BASE_URL + "/ship-address/ship-company";//获取快递物流公司
    public static final String GET_SEND_SHIP = BASE_URL + "/ship-address/send-ship";//发货
    public static final String GET_RECEIVE_SHIP = BASE_URL + "/ship-address/receive-ship";//确认收货


    /* ----------------  商家管理 ------------------*/
    public static final String POST_BUSINESS_ADD = BASE_URL + "/business/add";//商家认证
    public static final String POST_BUSINESS_RE_AUTH = BASE_URL + "/business/re-auth";//重新认证
    public static final String POST_BUSINESS_UPDATE = BASE_URL + "/business/update";//商家认证
    public static final String GET_BUSINESS_LIST = BASE_URL + "/business/list";//商家列表
    public static final String GET_BUSINESS_DETAIL = BASE_URL + "/business/detail";//商家认证
    public static final String GET_BUSINESS_CLOSE = BASE_URL + "/business/close";//商家认证


    /* ----------------  订单管理 ------------------*/
    public static final String GET_ORDER_CHECK = BASE_URL + "/order/check";//sku库存检测
    public static final String POST_ORDER_BUY = BASE_URL + "/order/buy";//立即结算
    public static final String POST_ORDER_CARTBUY = BASE_URL + "/order/cart-buy";//购物车结算
    public static final String GET_ORDER_LIST = BASE_URL + "/order/list";//订单列表
    public static final String GET_ORDER_BUSINESSLIST = BASE_URL + "/order/business-order";//订单列表
    public static final String GET_ORDER_CANCEL_ORDER = BASE_URL + "/order/cancel-order";//取消订单
    public static final String POST_ORDER_COMMENT = BASE_URL + "/order/comment";//订单评论
    public static final String GET_ORDER_DETIAl = BASE_URL + "/order/detail";//订单详情
    public static final String GET_WX_SIGN = IMAGE_URL + "/pay/wx-sign";//wx签名获取


    /* ----------------  登录管理 ------------------*/
    public static final String GET_OTHER_LOGIN = BASE_URL + "/user/other-login";//第三方登录
    public static final String GET_SEND_MESSAGE = BASE_URL + "/user/send-message";//发送验证码
    public static final String GET_REGISTER = BASE_URL + "/user/register";//手机号注册
    public static final String GET_LOGIN = BASE_URL + "/user/login";//账号密码
    public static final String GET_MESSAGE_LOGIN = BASE_URL + "/user/message-login";//手机号登陆
    public static final String GET_FORGET_PASS = BASE_URL + "/user/forget-pass";//忘记密码
    public static final String GET_USER_BIND = BASE_URL + "/user/user-bind";//第三方登录绑定
    public static final String GET_USER_INFO = BASE_URL + "/user/info";//获取个人信息
    public static final String POST_USER_UPDATA = BASE_URL + "/user/update";//更新个人信息

}
