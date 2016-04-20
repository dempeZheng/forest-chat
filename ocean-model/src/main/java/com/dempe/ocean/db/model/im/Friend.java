package com.dempe.ocean.db.model.im;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 好友关系文档集
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
@Entity("friend")
public class Friend {

    // uid&friendUid联合主键
    @Id
    private String id;
    private Long uid;
    private Long friendUid;
    private Long applyTime;//申请时间
    private String applyMsg;//申请信息
    private Long acceptTime;//接受时间
    private Integer stat;// 状态 好友，

    public static enum StatCode {
        FRIEND(0), STRANGER(1), BLACKLIST(2);
        private int stat;

        private StatCode(int stat) {
            this.stat = stat;
        }

        public int getStat() {
            return stat;
        }

        public void setStat(int stat) {
            this.stat = stat;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(Long friendUid) {
        this.friendUid = friendUid;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyMsg() {
        return applyMsg;
    }

    public void setApplyMsg(String applyMsg) {
        this.applyMsg = applyMsg;
    }

    public Long getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Long acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }
}
