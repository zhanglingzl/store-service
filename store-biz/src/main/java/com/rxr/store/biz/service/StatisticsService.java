package com.rxr.store.biz.service;

import com.rxr.store.common.dto.TradingVolume;

import java.util.List;

/**
 * @author ZL
 * @date 2018-10-22 15:45
 **/
public interface StatisticsService {

    /**
     * 获取当月上月交易信息
     *
     * @param agencyId 代理ID
     * @return 交易信息list
     */
    List<TradingVolume> getCurrentAndPreTradingVolume(Long agencyId);

}
