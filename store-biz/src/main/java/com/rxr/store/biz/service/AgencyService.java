package com.rxr.store.biz.service;

import com.rxr.store.common.entities.Agency;

public interface AgencyService {
    Agency findAgencyByWechatId(String wechatId);
}
