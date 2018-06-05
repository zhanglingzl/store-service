package com.rxr.store.biz.repositories;


import com.rxr.store.common.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long>{
    Optional<Agency> findByWechatId(String wechatId);
}
