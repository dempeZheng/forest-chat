package com.dempe.ocean.db.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
@Entity(value = "user")
public class User implements Serializable{
    @Id
    private Long uid;
    private String pwd;
    private String userName;
    private String profile;
    private String lastLoginTime;//最后登录时间
    private String mobileClientID;//手机端id
    private String clientID;//pc客户端id

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobileClientID() {
        return mobileClientID;
    }

    public void setMobileClientID(String mobileClientID) {
        this.mobileClientID = mobileClientID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", pwd='" + pwd + '\'' +
                ", userName='" + userName + '\'' +
                ", profile='" + profile + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", mobileClientID='" + mobileClientID + '\'' +
                ", clientID='" + clientID + '\'' +
                '}';
    }
}
