package com.rxr.store.web.job;

import com.rxr.store.biz.service.TradeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

/**
 * 定时任务清楚超过时间的未支付的交易
 */
@Service
public class PayStatusJob extends QuartzJobBean {

    @Autowired
    private TradeService tradeService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        tradeService.removeExpireTimeTrade();

    }
}
