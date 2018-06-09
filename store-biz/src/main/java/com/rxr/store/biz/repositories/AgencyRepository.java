package com.rxr.store.biz.repositories;

import com.rxr.store.common.entities.Agency;

import java.util.Optional;

/**
 * @author zero
 * @date Create in 2018/6/2 14:52
 */
public interface AgencyRepository extends BaseRepository<Agency, Long>{
    Optional<Agency> findByWechatId(String wechatId);
}
