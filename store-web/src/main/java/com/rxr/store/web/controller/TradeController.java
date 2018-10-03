package com.rxr.store.web.controller;

import com.rxr.store.biz.service.TradeService;
import com.rxr.store.common.dto.PageData;
import com.rxr.store.common.dto.PageParams;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Trade;
import com.rxr.store.common.form.TradeForm;
import com.rxr.store.web.common.dto.RestResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {
    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/trade/list")
    public RestResponse<PageData<Trade>> listTrades(TradeForm tradeForm, PageParams pageParams) {
        Agency agency = (Agency) SecurityUtils.getSubject().getPrincipal();
        tradeForm.setAgency(agency);
        if(tradeForm.getAgencyFlag() != null) {
            List<Long> agencyIds = tradeService.listAgencyIds(tradeForm.getAgency());
            if(agencyIds.size() == 0) {
                return RestResponse.success();
            }
            tradeForm.setAgencyIds(agencyIds);
        }

        Page<Trade> trades = tradeService.listTrades(tradeForm, pageParams);
        return RestResponse.success(PageData.bulid(trades));
    }

    @GetMapping("/trade/detail/{tradeNo}")
    public RestResponse<Trade> findTradeByTradeNo(@PathVariable("tradeNo") String tradeNo) {
        Trade trade = this.tradeService.findTradeByTradeNo(tradeNo);
        return RestResponse.success(trade);
    }

    @GetMapping("/admin/trade/list")
    public RestResponse<List<Trade>> findAllTrades(TradeForm tradeForm) {
        List<Trade> trades = this.tradeService.findAllTrades(tradeForm);
        return RestResponse.success(trades);
    }
}
