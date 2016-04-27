package com.dempe.logic.server.dao;

import com.dempe.ocean.db.model.im.UserGroup;
import com.mongodb.WriteResult;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserGroupDao extends BasicDAO<UserGroup, Serializable> {

    @Autowired
    protected UserGroupDao(Datastore dataStore) {
        super(dataStore);
        ensureIndexes();// 自动创建索引
    }

    public List<UserGroup> listUserGroupByGroupId(String groupId) {
        return createQuery().field("groupId").equal(groupId).asList();
    }

    public WriteResult delUserGroup(String groupId, Long uid) {
        Query<UserGroup> query = createQuery().field("groupId").equal(groupId).field("uid").equal(uid);
        return deleteByQuery(query);
    }

}