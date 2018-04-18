package com.rxr.store.biz.repositories;

import com.rxr.store.common.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByLoginName(String loginName);
}
