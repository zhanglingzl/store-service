package com.rxr.store.wechat.repositories;

import com.rxr.store.common.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface WechatAuthRepository extends JpaRepository<Agency, Long> {
    Agency findAgencyByWechatId(String wechatId);
    Agency findAgencyById(Long parentId);

    @Modifying
    @Query("update Agency set level=?1 where id=?2")
    int updateAgencyLevelById(int level, Long id);
}
