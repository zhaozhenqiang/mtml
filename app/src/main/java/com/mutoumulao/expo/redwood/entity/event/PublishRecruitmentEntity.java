package com.mutoumulao.expo.redwood.entity.event;

/**
 * Created by lzy on 2018/8/12.
 * 发布招聘，服务，求职 回调刷新列表事件
 */

public class PublishRecruitmentEntity {

    public String type;

    public PublishRecruitmentEntity() {
    }

    public PublishRecruitmentEntity(String type) {
        this.type = type;
    }
}
