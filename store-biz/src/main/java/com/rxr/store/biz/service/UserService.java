package com.rxr.store.biz.service;

import com.rxr.store.common.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<User> findAllUser();
    User findUserByLoginName(String loginName);
    Page<User> findAllUser(User user);
}
