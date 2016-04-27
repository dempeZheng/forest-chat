package com.dempe.logic.server.bussiness;

import com.dempe.logic.server.dao.UserGroupDao;
import com.dempe.ocean.db.model.im.UserGroup;
import com.google.common.collect.Lists;
import com.mongodb.WriteResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/10
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserGroupBusiness {

    @Resource
    private UserGroupDao userGroupDao;

    public List<Long> listUidByGroupId(String groupId) {
        List<UserGroup> userGroupList = userGroupDao.listUserGroupByGroupId(groupId);
        List<Long> uidList = Lists.newArrayList();
        for (UserGroup userGroup : userGroupList) {
            uidList.add(userGroup.getUid());
        }
        return uidList;

    }

    public void saveUserGroup(UserGroup userGroup) {
        userGroupDao.save(userGroup);
    }

    public WriteResult delUserGroup(String groupId, Long uid) {
        return userGroupDao.delUserGroup(groupId, uid);
    }


}
