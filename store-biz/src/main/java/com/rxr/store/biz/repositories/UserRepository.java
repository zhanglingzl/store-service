package com.rxr.store.biz.repositories;

import com.rxr.store.common.entity.User;


public interface UserRepository extends BaseRepository<User, Long> {
    User findByLoginName(String loginName);
}
