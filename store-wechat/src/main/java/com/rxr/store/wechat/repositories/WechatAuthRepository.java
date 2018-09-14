package com.rxr.store.wechat.repositories;

import com.rxr.store.common.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WechatAuthRepository extends JpaRepository<Agency, Long> {
    Agency findAgencyByWechatId(String wechatId);
    Agency findAgencyById(Long parentId);
}
