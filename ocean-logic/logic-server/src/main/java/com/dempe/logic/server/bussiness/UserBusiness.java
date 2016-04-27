package com.dempe.logic.server.bussiness;

import com.dempe.logic.server.dao.UserDao;
import com.dempe.ocean.db.model.User;
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
public class UserBusiness {

    @Resource
    private UserDao userDao;

    public User login(String uid, String pwd) {
        return userDao.login(uid, pwd);
    }

    public void save(User user) {
        userDao.save(user);
    }

}
