package com.rxr.store.wechat.repositories;

import com.rxr.store.common.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WechatAuthRepository extends JpaRepository<Agency, Long> {
}
