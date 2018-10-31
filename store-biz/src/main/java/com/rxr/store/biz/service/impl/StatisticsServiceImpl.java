package com.rxr.store.biz.service.impl;

import com.google.common.collect.Lists;
import com.rxr.store.biz.repositories.TradeRepository;
import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.biz.service.StatisticsService;
import com.rxr.store.common.dto.TradingVolume;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ZL
 * @date 2018-10-22 15:48
 **/
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public List<TradingVolume> getCurrentAndPreTradingVolume(Long agencyId) {
        List<TradingVolume> result = Lists.newArrayList();
        List<Agency> agencies = Lists.newArrayList(agencyService.findAgencyById(agencyId));
        List<Integer> payStatus = Lists.newArrayList(1, 2);
        List<Trade> tradeList = tradeRepository.findByPayStatusInAndAgencyInAndCreateTimeBetween(payStatus, agencies, DateHelper.getMonthFirstDayToDate(-1), DateHelper.getMonthLastDayToDate(0));
        tradeList.forEach(trade -> trade.setCreateTime(DateHelper.convertDate(trade.getCreateTime())));
        tradeList.stream().collect(Collectors.groupingBy(Trade::getCreateTime))
                .forEach((key, value) -> {
                    TradingVolume tradingVolume = new TradingVolume();
                    Optional<Integer> count = value.stream().map(Trade::getTotalCount).reduce((i, j) -> i + j);
                    Optional<BigDecimal> totalAmount = value.stream().map(Trade::getTotalAmount).reduce(BigDecimal::add);
                    tradingVolume.setTotalCount(count.get());
                    tradingVolume.setTotalAmount(String.valueOf(totalAmount.get()));
                    tradingVolume.setDate(DateHelper.format(key, DateHelper.FORMAT_YM_PATTERN));
                    result.add(tradingVolume);
                });
        result.sort(Comparator.comparing(TradingVolume::getDate));
        return result;
    }
}
