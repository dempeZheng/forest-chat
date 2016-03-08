package com.dempe.ocean.node.live.service;

import com.dempe.ocean.node.live.dao.LiveChannelDao;
import com.dempe.ocean.node.live.model.LiveChannel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
@Service
public class LiveChannelService {

    @Resource
    private LiveChannelDao liveChannelDao;


    public void delChannel(Long topSid, Long subSid) {
        liveChannelDao.delChannel(topSid, subSid);
    }

    public void saveChannel(Long uid, Long topSid, Long subSid, String name, String profile) {
        LiveChannel channel = new LiveChannel();
        channel.setCreateByUid(uid);
        channel.setCreateTime(System.currentTimeMillis());
        channel.setName(name);
        channel.setProfile(profile);
        channel.setSubSid(topSid);
        channel.setSubSid(subSid);
        liveChannelDao.saveChannel(channel);
    }



}
