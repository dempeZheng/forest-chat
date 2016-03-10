package com.dempe.ocean.node.live.dao;

import com.dempe.ocean.common.model.live.LiveChannel;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class LiveChannelDao extends BasicDAO<LiveChannel, Serializable> {

    @Autowired
    protected LiveChannelDao(Datastore dataStore) {
        super(dataStore);
        ensureIndexes();// 自动创建索引
    }

    public void saveChannel(LiveChannel channel) {
        save(channel);
    }

    public void delChannel(Long topSid, Long subSid) {
        deleteByQuery(createQuery().field("topSid").equal(topSid).field("subSid").equal(subSid));
    }
}
