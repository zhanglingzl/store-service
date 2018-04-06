package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.mapper.UserMapper;
import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }
}
