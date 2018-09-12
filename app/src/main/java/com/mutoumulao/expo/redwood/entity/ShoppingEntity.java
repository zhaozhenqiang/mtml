package com.mutoumulao.expo.redwood.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzy on 2018/8/9.
 */

public class ShoppingEntity implements Serializable{
    /**
     * {
     * "code": 200,
     * "msg": "查询成功",
     * "data": [{
     * "id": "1",
     * "business_name": "测试商家",
     * "avatar": "/avatar/default.jpg",
     * "list": [{
     * "id": "2",
     * "author_id": "1",
     * "business_id": "1",
     * "goods_id": "8",
     * "num": "1",
     * "sku_id": "12",
     * "created_at": "2018-08-08 13:46:09",
     * "updated_at": "2018-08-08 22:45:38",
     * "spec": "红色:10寸",
     * "price": "30.00",
     * "stock": "40",
     * "goods_name": "韭菜盒子123",
     * "category": "家具",
     * "category_type": "桌类,鸡翅,成品",
     * "goods_image": ["/upload/18/08/04/10/e8fa2555e277a155fe8eeb1df0e3c4ff.jpg", "/upload/18/08/08/14/6b6ffed7d7a5371a633aa317989c407c.jpg"],
     * "desc": "测试商品描述撒打算打",
     * "desc_image": ["/upload/18/08/04/10/e8fa2555e277a155fe8eeb1df0e3c4ff.jpg"],
     * "freight": "201",
     * "send_address": "河北省,唐山市,丰润区"
     * }]
     * }]
     * }
     */
    public String id;
    public String business_name;
    public String avatar;
    public List<ShoppingCarEntity> list;

    public List<ShoppingCarEntity> getList() {
        return list;
    }

    public void setList(List<ShoppingCarEntity> list) {
        this.list = list;
    }

    public static class ShoppingCarEntity implements Serializable{
        public String id;
        public String author_id;
        public Object business_id;
        public String goods_id;
        public String num;
        public String sku_id;
        public String created_at;
        public String updated_at;
        public String spec;
        public String price;
        public String stock;
        public String goods_name;
        public String category;
        public String category_type;
        public String desc;
        public String freight;
        public String send_address;
        public List<String> goods_image;
        public List<String> desc_image;
    }

}
