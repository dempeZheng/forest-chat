package com.dempe.ocean.logic.common.dao;

import com.dempe.ocean.db.model.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDao extends BasicDAO<User, Serializable> {

    @Autowired
    protected UserDao(Datastore dataStore) {
        super(dataStore);
        ensureIndexes();// 自动创建索引
    }

    public User login(String uid, String pwd) {
        return createQuery().field("uid").equal(uid).field("pwd").equal(pwd).get();
    }



}

