package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.UserRepository;
import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByLoginName(String loginName) {
        return userRepository.findByLoginName(loginName);
    }
}
