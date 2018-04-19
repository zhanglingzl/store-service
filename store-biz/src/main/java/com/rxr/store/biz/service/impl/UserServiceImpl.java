package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.UserRepository;
import com.rxr.store.biz.service.UserService;
import com.rxr.store.common.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.List;

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
    public Page<User> findAllUser(User user) {
        Pageable pageable = PageRequest.of(0,10, new Sort(Sort.Direction.ASC,"id"));
        Page<User> page = userRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(StringUtils.isNotBlank(user.getLoginName())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("loginName"),"%"+user.getLoginName()+"%"));
            }
            if(StringUtils.isNotBlank(user.getName())) {
                predicate.getExpressions()
                        .add(criteriaBuilder.like(root.get("name"),user.getName()));
            }
            if(user.getState() != null) {
                predicate.getExpressions()
                        .add(criteriaBuilder.equal(root.get("state"),user.getState()));
            }
            return predicate;
        }, pageable);
        return page;
    }
}
