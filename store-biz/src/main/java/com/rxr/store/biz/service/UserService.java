package com.rxr.store.biz.service;

import com.rxr.store.common.entity.User;
import com.rxr.store.common.form.UserForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> findAllUser();
    User findUserByLoginName(String loginName);
    Page<User> findAllUser(UserForm user, Pageable pageable);
    User findUserById(Long id);
}
