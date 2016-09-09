package com.dempe.ocean.http.controller;

import com.dempe.logic.api.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/9/9
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/userGroup")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;


}
