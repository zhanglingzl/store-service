package com.rxr.store.web.controller;

import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.PageData;
import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.web.common.dto.RestResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeController {
    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/trade/list")
    public RestResponse<PageData<Trade>> listTrades(Trade trade, PageParams pageParams) {
        Agency agency = (Agency) SecurityUtils.getSubject().getPrincipal();
        trade.setAgency(agency);
        Page<Trade> trades = tradeService.listTrades(trade, pageParams);
        return RestResponse.success(PageData.bulid(trades));
    }
}
