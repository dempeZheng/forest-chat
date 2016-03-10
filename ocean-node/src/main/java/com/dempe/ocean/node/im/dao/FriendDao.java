package com.dempe.ocean.node.im.dao;

import com.dempe.ocean.common.model.im.Friend;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class FriendDao extends BasicDAO<Friend, Serializable> {

    @Autowired
    protected FriendDao(Datastore dataStore) {
        super(dataStore);
        ensureIndexes();// 自动创建索引
    }

    // 接受申请
    public void acceptFriend(Long uid, Long friendUid) {
        Query<Friend> query = createQuery().field("uid").equal(uid).field("friendUid").equal(friendUid);
        UpdateOperations<Friend> opt = createUpdateOperations().set("acceptTime", System.currentTimeMillis())
                .set("stat", Friend.StatCode.FRIEND);
        updateFirst(query, opt);
    }

    // 申请好友
    public void applyFriend(Long uid, Long friendUid, String applyMsg) {
        Friend friend = new Friend();
        friend.setUid(uid);
        friend.setFriendUid(friendUid);
        friend.setApplyMsg(applyMsg);
        friend.setApplyTime(System.currentTimeMillis());
        friend.setStat(Friend.StatCode.STRANGER.getStat());
        save(friend);
    }

    public void delFriend(Long uid, Long friendUid) {
        Query<Friend> query = createQuery().field("uid").equal(uid).field("friendUid").equal(friendUid);
        deleteByQuery(query);
    }

}

