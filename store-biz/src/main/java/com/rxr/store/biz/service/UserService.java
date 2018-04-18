package com.rxr.store.biz.service;

import com.rxr.store.common.entities.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();
    User findUserByLoginName(String loginName);
}
