package com.dempe.ocean.logic.im.service;

import com.dempe.ocean.logic.im.dao.IMGroupDao;
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
public class IMGroupService {

    @Resource
    private IMGroupDao imGroupDao;


    public void saveUserGroup(Long createByUid, String groupName, String groupProfile) {
        imGroupDao.saveUserGroup(createByUid, groupName, groupProfile);
    }



}
