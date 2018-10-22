package com.rxr.store.biz.service;

import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.dto.TransactionStatisticsDTO;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.form.TradeForm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TradeService {
    void save(Trade trade);

    void updateTrade(Trade trade);

    Page<Trade> listTrades(TradeForm trade, PageParams pageParams);

    List<Long> listAgencyIds(Agency agency);

    Double[] getSemiannualTrade(List<Agency> agencies, int month);

    Trade findTradeByTradeNo(String tradeNo);

    void removeExpireTimeTrade();

    Double getTotalAmount(Agency agency);

    List<Trade> findAllTrades(TradeForm tradeForm);

    void updateShipping(TradeForm tradeForm);

    TransactionStatisticsDTO findTransactionStatistics();

    void updateAgencyLevelByTrade();
}
