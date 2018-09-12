package com.mutoumulao.expo.redwood.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzy on 2018/8/15.
 */

public class GoodsDetialEntity implements Serializable{

    /**
     * id : 10
     * author_id : 1
     * goods_name : 高级椅子
     * category : 家具
     * category_type : 椅凳,鸡翅,光身
     * desc : 描述 描述 自己的哈就是金发科技符合撒
     * desc_image : ["/upload/18/08/04/08/175ed614200942d9a33aa12370722050.jpg","/upload/18/08/04/08/8318dd02c7290deb671afa38ded7c127.jpg","/upload/18/08/04/08/8605ecf73aa5d1a46d382d279833fa28.jpg","/upload/18/08/04/08/d096076eda56466dca38b263dd2f7aac.jpg","/upload/18/08/04/08/ce90cdcdf74e8e9eb8f1e6883dfa8472.jpg"]
     * goods_image : ["/upload/18/08/04/08/2ba6805665d2bd782dba6be875035f4b.jpg","/upload/18/08/04/08/06f48e56bfd95ac17e48bccc4218cbb5.jpg"]
     * freight : 12345
     * send_address : 河北省,沧州市,运河区
     * comment_num : 0
     * sku_name : 高度,长度
     * sku_property : 10cm:100cm
     * price : 10.00
     * status : 1
     * sale_num : null
     * created_at : 2018-08-04 08:58:45
     * updated_at : 2018-08-10 23:30:14
     * business_name : 测试商家
     avatar: "/avatar/default.jpg",
     * minPrice : 10.00
     * maxPrice : 70.00
     * skuList : [{"sku_id":"15","spec":"10cm:100cm","sku_name":"高度,长度","price":"10.00","stock":"20"},{"sku_id":"16","spec":"20cm:100cm","sku_name":"高度,长度","price":"30.00","stock":"40"},{"sku_id":"17","spec":"10cm:200cm","sku_name":"高度,长度","price":"50.00","stock":"60"},{"sku_id":"18","spec":"20cm:200cm","sku_name":"高度,长度","price":"70.00","stock":"80"}]
     * guiges : [{"title":"高度","guigeArray":["10cm","20cm"]},{"title":"长度","guigeArray":["100cm","200cm"]}]
     "desc_star": 0,
     "wuliu_star": 0,
     "fuwu_star": 0,
     "goods_count": "2"
     */

    public String id;
    public String author_id;
    public String goods_name;
    public String category;
    public String category_type;
    public String desc;
    public String freight;
    public String send_address;
    public String comment_num;
    public String sku_name;
    public String sku_property;
    public String price;
    public String status;
    public String sale_num;
    public String created_at;
    public String updated_at;
    public String business_name;
    public String business_phone;
    public String business_id;
    public String minPrice;
    public String maxPrice;
    public List<String> desc_image;
    public List<String> goods_image;
    public List<SkuListEntity> skuList;
    public List<GuigesEntity> guiges;
    public String avatar;
    public String desc_star;
    public String wuliu_star;
    public String fuwu_star;
    public String goods_count;


    public static class SkuListEntity implements Serializable{
        /**
         * sku_id : 15
         * spec : 10cm:100cm
         * sku_name : 高度,长度
         * price : 10.00
         * stock : 20
         */

        public String sku_id;
        public String spec;
        public String sku_name;
        public String price;
        public String stock;

    }

    public static class GuigesEntity implements Serializable{
        /**
         * title : 高度
         * guigeArray : ["10cm","20cm"]
         */

        public String title;
        public List<String> guigeArray;

    }
}
