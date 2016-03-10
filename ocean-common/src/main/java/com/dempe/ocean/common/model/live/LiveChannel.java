package com.dempe.ocean.common.model.live;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 直播频道
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
@Entity("live_channel")
public class LiveChannel {
    @Id
    private Long topSid;
    private Long subSid;
    private Long createByUid;
    private Long createTime;
    private String name;
    private String profile;

    public Long getTopSid() {
        return topSid;
    }

    public void setTopSid(Long topSid) {
        this.topSid = topSid;
    }

    public Long getSubSid() {
        return subSid;
    }

    public void setSubSid(Long subSid) {
        this.subSid = subSid;
    }

    public Long getCreateByUid() {
        return createByUid;
    }

    public void setCreateByUid(Long createByUid) {
        this.createByUid = createByUid;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
