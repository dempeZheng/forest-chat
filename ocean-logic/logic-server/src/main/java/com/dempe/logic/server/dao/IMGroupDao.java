package com.dempe.logic.server.dao;

import com.dempe.ocean.db.model.im.IMGroup;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
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
public class IMGroupDao extends BasicDAO<IMGroup, Serializable> {

    @Autowired
    protected IMGroupDao(Datastore dataStore) {
        super(dataStore);
        ensureIndexes();// 自动创建索引
    }


    public void saveUserGroup(Long createByUid, String groupName, String groupProfile) {
        IMGroup group = new IMGroup();
        group.setCreateByUid(createByUid);
        group.setGroupName(groupName);
        group.setGroupProfile(groupProfile);
        group.setCreateTime(System.currentTimeMillis());
        save(group);
    }


}

