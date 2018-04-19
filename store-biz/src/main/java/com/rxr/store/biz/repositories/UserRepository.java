package com.rxr.store.biz.repositories;

import com.rxr.store.common.entities.User;


public interface UserRepository extends BaseRepository<User, Long> {
    User findByLoginName(String loginName);
}
