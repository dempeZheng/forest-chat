package com.dempe.ocean.common.model.im;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 用户群文档集
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
@Entity("im_group")
public class IMGroup {
    @Id
    private String userGroupId;
    private Long createByUid;
    private String groupName;
    private String groupProfile;
    private long createTime;

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getCreateByUid() {
        return createByUid;
    }

    public void setCreateByUid(Long createByUid) {
        this.createByUid = createByUid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(String groupProfile) {
        this.groupProfile = groupProfile;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
