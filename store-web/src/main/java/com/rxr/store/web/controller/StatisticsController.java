package com.rxr.store.web.controller;

import com.rxr.store.biz.service.StatisticsService;
import com.rxr.store.common.dto.TradingVolume;
import com.rxr.store.web.common.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计controller
 *
 * @author ZL
 * @date 2018-10-22 15:10
 **/
@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping("/statistics/{agencyId}")
    public RestResponse<List<TradingVolume>> getCurrentAndPreTradingVolume(@PathVariable("agencyId") Long agencyId){
        return RestResponse.success(service.getCurrentAndPreTradingVolume(agencyId));
    }

}
