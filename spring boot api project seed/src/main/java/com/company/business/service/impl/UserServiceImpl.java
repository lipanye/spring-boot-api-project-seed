package com.company.business.service.impl;

import com.company.business.dao.UserMapper;
import com.company.business.model.User;
import com.company.business.service.UserService;
import com.company.system.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/03/18.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
