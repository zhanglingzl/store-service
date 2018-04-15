package com.rxr.store.web.service.impl;

import com.google.common.base.Strings;
import com.rxr.store.common.model.Guest;
import com.rxr.store.web.repository.IGuestRepository;
import com.rxr.store.web.service.IGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zero
 */
@Service
public class GuestServiceImpl implements IGuestService {

    @Autowired
    private IGuestRepository guestRepository;

    @Override
    public List<Guest> findSearch(Guest guest) {
        return guestRepository.findAll((Specification<Guest>) (Root<Guest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->{

          List<Predicate> list = new ArrayList<>();
          if(!Strings.isNullOrEmpty(guest.getName())){
            list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + guest.getName() + "%"));
          }
          if(!Strings.isNullOrEmpty(guest.getWeChatNo())){
            list.add(criteriaBuilder.like(root.get("weChatNo").as(String.class), "%" + guest.getWeChatNo()+ "%"));
          }
          if(!Strings.isNullOrEmpty(guest.getName())){
            list.add(criteriaBuilder.like(root.get("telephone").as(String.class), "%" + guest.getTelephone() + "%"));
          }
          Predicate[] predicates = new Predicate[list.size()];
          return query.where(list.toArray(predicates)).getRestriction();
      });
    }
}
