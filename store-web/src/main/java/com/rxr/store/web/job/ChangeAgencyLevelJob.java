package com.rxr.store.web.job;

import com.rxr.store.biz.service.TradeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

@Service
public class ChangeAgencyLevelJob extends QuartzJobBean {
    @Autowired
    private TradeService tradeService;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.tradeService.updateAgencyLevelByTrade();
    }
}
