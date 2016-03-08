package com.dempe.ocean.node.im.dao;

import com.dempe.ocean.node.im.model.UserGroup;
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
public class UserGroupDao extends BasicDAO<UserGroup, Serializable> {

    @Autowired
    protected UserGroupDao(Datastore dataStore) {
        super(dataStore);
        ensureIndexes();// 自动创建索引
    }


    public void saveUserGroup(Long createByUid, String groupName, String groupProfile) {
        UserGroup group = new UserGroup();
        group.setCreateByUid(createByUid);
        group.setGroupName(groupName);
        group.setGroupProfile(groupProfile);
        group.setCreateTime(System.currentTimeMillis());
        save(group);
    }


}

