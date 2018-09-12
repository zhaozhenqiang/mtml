package com.mutoumulao.expo.redwood.entity;

import java.io.Serializable;

/**
 * Created by lzy on 2018/8/3.
 */

public class PositionEntity implements Serializable{
    /**
     *
     "id": "1",
     "author_id": "1",
     "title": "测试的木头木老",
     "category_id": "1",
     "category_name": "运输",
     "content": "测试的木头木老测试的木头木老测试的木头木老测试的木头木老测试的木头木老测试的木头木老",
     "images": [
     "/avatar/default.jpg",
     "/avatar/default.jpg",
     "/avatar/default.jpg"
     ],
     "nickname": "木头木头         "
     */
    public String id;
    public String author_id;
    public String title;
    public String category_id;
    public String category_name;
    public String content;
    public String images;
    public String nickname;
    public String time;
    public String phone;
    public String created_at;
    public String updated_at;
    public String avatar;
}
