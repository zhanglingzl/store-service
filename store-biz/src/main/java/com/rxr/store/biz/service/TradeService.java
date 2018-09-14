package com.rxr.store.biz.service;

import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.entity.Trade;
import org.springframework.data.domain.Page;

public interface TradeService {
    void save(Trade trade);

    void updateTrade(Trade trade);

    Page<Trade> listTrades(Trade trade, PageParams pageParams);
}
