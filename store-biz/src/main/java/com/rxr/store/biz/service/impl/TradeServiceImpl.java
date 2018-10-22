package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.TradeRepository;
import com.rxr.store.biz.service.AgencyService;
import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.dto.TransactionStatisticsDTO;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.form.TradeForm;
import com.rxr.store.common.util.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final AgencyService agencyService;

    @Autowired
    public TradeServiceImpl(TradeRepository tradeRepository, AgencyService agencyService) {
        this.tradeRepository = tradeRepository;
        this.agencyService = agencyService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Trade trade) {
        this.tradeRepository.save(trade);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTrade(Trade trade) {
        this.tradeRepository.updateTrade(trade.getPayStatus(),trade.getTransactionId()
                ,trade.getBankType(),trade.getPayEndTime(),trade.getTradeNo());
    }

    @Override
    public Page<Trade> listTrades(TradeForm trade, PageParams pageParams) {
        return tradeRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(trade.getPayStatus() != null && !trade.getPayStatus().equals(-3)) {
                predicate.getExpressions()
                        .add(criteriaBuilder.equal(root.get("payStatus"),trade.getPayStatus()));
            }
            Join<Trade, Agency> agencyJoin = root.join("agency");
            if(trade.getAgencyIds() != null) {
                predicate.getExpressions().add(root.get("payStatus").in(1,2));
                predicate.getExpressions().add(agencyJoin.get("id").in(trade.getAgencyIds()));
            } else {
                predicate.getExpressions().add(criteriaBuilder.equal(agencyJoin.get("id"), trade.getAgency().getId()));
            }
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return predicate;
        }, pageParams.pageable());
    }

    @Override
    public List<Long> listAgencyIds(Agency agency) {
        Pair<List<Long>, int[]> agencyIdPair = agencyService.findAgencyByCount(agency);
        return agencyIdPair.getKey();
    }

    @Override
    public Double[] getSemiannualTrade(List<Agency> agencies, int month) {
        Double[] tradeAmount = new Double[month+1];
        for (int i=0; i<month+1; i++) {
            tradeAmount[i] = 0d;
        }
        Date before = DateHelper.minusMonths(DateHelper.getFirstDayofMounth(), month);
        Date after = DateHelper.getLastDayofMounth();
        List<Trade> trades = this.tradeRepository.findAllByPayStatusAndAgencyInAndCreateTimeBetween(1,
                agencies,before,after);
        Map<String, BigDecimal> tradeMap = trades.stream().collect(Collectors.groupingBy((Trade trade) -> DateHelper
                .format(trade.getCreateTime(),"yyyy-MM"),
                Collectors.mapping(Trade::getPayableAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        int flag = 0;
        do {
            String key = DateHelper.format(after, "yyyy-MM");
            BigDecimal value = tradeMap.get(key);
            if(value != null) {
                tradeAmount[flag] = value.doubleValue();
            }
            flag++;
            after = DateHelper.minusMonths(after,1);
        } while (flag <= month);
        return tradeAmount;
    }

    @Override
    public Trade findTradeByTradeNo(String tradeNo) {
        return tradeRepository.findTradeByTradeNo(tradeNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeExpireTimeTrade() {
        Date before = DateHelper.minusMinutes(new Date(), 30);
        log.info(DateHelper.format(before,"yyyy-MM-dd HH:mm:ss"));
        List<Trade> expireTimeTrades = tradeRepository.findAllByPayStatusAndCreateTimeBefore(0, before);
        expireTimeTrades.forEach(trade -> {
            log.info("超过时间没有支付的订单: {}", trade.toString());
            //this.tradeRepository.delete(trade);
            this.tradeRepository.updatePayStatusByTradeNo(-2, trade.getTradeNo());

        });
    }

    @Override
    public Double getTotalAmount(Agency agency) {
        return tradeRepository.findTradeByAgencyAndPayStatus(agency, 1);
    }

    @Override
    public List<Trade> findAllTrades(TradeForm tradeForm) {
        return this.tradeRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(tradeForm.getTradeNo()) && !"null".equals(tradeForm.getTradeNo())) {
               predicate.getExpressions().add(criteriaBuilder.like(root.get("tradeNo"),"%"+tradeForm.getTradeNo()+"%"));
            }
            if(tradeForm.getBetweenDate() != null && tradeForm.getBetweenDate().length == 2) {
                predicate.getExpressions().add(criteriaBuilder.between(root.get("createTime"),
                        DateHelper.format(tradeForm.getBetweenDate()[0],"yyyy-MM-dd HH:mm:ss"),
                        DateHelper.format(tradeForm.getBetweenDate()[1],"yyyy-MM-dd HH:mm:ss")));
            }
            if(StringUtils.isNotBlank(tradeForm.getTelephone()) && !"null".equals(tradeForm.getTelephone())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("phone"), "%"+tradeForm.getTelephone()+"%"));
            }

            if(StringUtils.isNotBlank(tradeForm.getAddress()) && !"null".equals(tradeForm.getAddress())) {
                predicate.getExpressions().add(criteriaBuilder.like(
                        criteriaBuilder.concat(criteriaBuilder.concat(criteriaBuilder.concat(root.get("province"),root.get("city"))
                                ,root.get("country")), root.get("address")),"%"+tradeForm.getAddress()+"%"));
            }
            if(StringUtils.isNotBlank(tradeForm.getTrackingNo()) && !"null".equals(tradeForm.getTrackingNo())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("trackingNo"),tradeForm.getTrackingNo()));
            }
            if(StringUtils.isNotBlank(tradeForm.getShipStatus()) && !"null".equals(tradeForm.getShipStatus())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("shipStatus"), tradeForm.getShipStatus()));
            }
            Join<Trade, Agency> agencyJoin = root.join("agency");
            if(StringUtils.isNotBlank(tradeForm.getAgencyName()) && !"null".equals(tradeForm.getAgencyName())) {
                predicate.getExpressions().add(criteriaBuilder.like(agencyJoin.get("name"),"%"+tradeForm.getAgencyName() +"%"));
            }
            if(StringUtils.isNotBlank(tradeForm.getAgencyId()) && !"null".equals(tradeForm.getAgencyId())) {
                predicate.getExpressions().add(criteriaBuilder.equal(agencyJoin.get("id"), tradeForm.getAgencyId()));
            }
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return predicate;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShipping(TradeForm tradeForm) {
        this.tradeRepository.updateShipping(tradeForm.getTrade().getTrackingName(), tradeForm.getTrade().getTrackingNo(),
                1, tradeForm.getTrade().getTradeNo());
    }

    @Override
    public TransactionStatisticsDTO findTransactionStatistics() {
        Date end = DateHelper.format(DateHelper
                .format(new Date(), "yyyy-MM-dd 23:59:59"),"yyyy-MM-dd HH:mm:ss");
        Date before = DateHelper.minusMonths(end, 1);
        TransactionStatisticsDTO statisticsDTO = new TransactionStatisticsDTO();
        Map<String ,Object> totalTrade = this.tradeRepository.findTradeByPayStatus(1);
        statisticsDTO.setTotalAmount((BigDecimal) totalTrade.get("amount"));
        statisticsDTO.setTotalCount((Long) totalTrade.get("totalCount"));
        List<Trade> tradeList = this.tradeRepository.findAllByPayStatusAndCreateTimeBetween(1, before, end);
        Map<String, List<Trade>> tradeMap = tradeList.stream().collect(Collectors.groupingBy((Trade trade) -> DateHelper
                        .format(trade.getCreateTime(),"yyyy-MM-dd")));
        Map<String, TransactionStatisticsDTO> statisticsDTOMap = new LinkedHashMap<>();
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAgencyLevelByTrade() {
        List<Trade> trades = this.tradeRepository.findTradeByPayStatusAndAgencyLevel(1, 0);
        Map<Long, BigDecimal> tradeMap = trades.stream().collect(Collectors.groupingBy((Trade trade) -> trade.getAgency().getId(),
                Collectors.mapping(Trade::getPayableAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        tradeMap.forEach((agencyId, amount) -> {
            if(new BigDecimal("300").subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
                log.info("交易金额 {} > 300, 会员ID {}", amount, agencyId);
                this.agencyService.updateAgencyLevelById(agencyId);
            }
        });
    }
}
