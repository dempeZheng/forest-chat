package com.dempe.ocean.logic.im.service;

import com.dempe.ocean.logic.im.dao.FriendDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FriendService {

    @Resource
    private FriendDao friendDao;

    /**
     * 接受申请
     *
     * @param uid
     * @param friendUid
     */
    public void acceptFriend(Long uid, Long friendUid) {
        friendDao.acceptFriend(uid, friendUid);
    }


    /**
     * 申请好友
     *
     * @param uid
     * @param friendUid
     * @param applyMsg
     */
    public void applyFriend(Long uid, Long friendUid, String applyMsg) {
        friendDao.applyFriend(uid, friendUid, applyMsg);
    }


    /**
     * 删除好友
     * @param uid
     * @param friendUid
     */
    public void delFriend(Long uid, Long friendUid) {
        friendDao.delFriend(uid,friendUid);
    }

}
