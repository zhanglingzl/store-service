package com.rxr.store.biz.service.impl;

import com.rxr.store.biz.repositories.TradeRepository;
import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    @Autowired
    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
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
    public Page<Trade> listTrades(Trade trade, PageParams pageParams) {
        return tradeRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(trade.getPayStatus() != null && !trade.getPayStatus().equals(-3)) {
                predicate.getExpressions()
                        .add(criteriaBuilder.equal(root.get("payStatus"),trade.getPayStatus()));
            }
            Join<Trade, Agency> agencyJoin = root.join("agency");
            predicate.getExpressions().add(criteriaBuilder.equal(agencyJoin.get("id"), trade.getAgency().getId()));
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return predicate;
        }, pageParams.pageable());
    }
}
