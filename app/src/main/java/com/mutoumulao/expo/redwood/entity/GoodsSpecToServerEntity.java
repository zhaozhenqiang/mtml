package com.mutoumulao.expo.redwood.entity;

import java.io.Serializable;

/**
 * Created by lzy on 2018/8/6.
 * 上传服务器数据类型
 */

public class GoodsSpecToServerEntity implements Serializable{
        /* "spec":"10寸,黑色",
           "price":"10",
           "stock":"5",
           "sku_name":"尺寸,颜色"
        */
    public String spec;
    public String price;
    public String stock;
    public String sku_name;


}
