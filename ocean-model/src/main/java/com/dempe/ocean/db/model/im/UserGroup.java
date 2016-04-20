package com.dempe.ocean.db.model.im;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
@Entity("user_group")
public class UserGroup {

    @Id
    private String id;
    private String groupId;
    private Long uid;
    private long createTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
