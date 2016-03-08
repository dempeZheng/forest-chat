package com.dempe.ocean.node.common.service;

import com.dempe.ocean.node.common.dao.UserDao;
import com.dempe.ocean.node.common.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/8
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public User login(String uid, String pwd) {
        return userDao.login(uid, pwd);
    }

}
