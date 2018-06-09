package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.UserRepository;
import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.entities.User;
import com.rxr.store.common.form.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;
    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByLoginName(String loginName) {
        return userRepository.findByLoginName(loginName);
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public Page<User> findAllUser(UserForm user, Pageable pageable) {
        return userRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(StringUtils.isNotBlank(user.getLoginName())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("loginName"),"%"+user.getLoginName()+"%"));
            }
            if(StringUtils.isNotBlank(user.getName())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("name"),"%"+user.getName()+"%"));
            }
            if(user.getState() != null) {
                predicate.getExpressions()
                        .add(criteriaBuilder.equal(root.get("state"),user.getState()));
            }
            return predicate;
        }, pageable);
    }

}
