package com.rxr.store.web.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
@ConfigurationProperties
public class SchedulerFactoryConfig implements ApplicationListener<ContextRefreshedEvent> {
    private Map<String, String> tasks = new HashMap<>();

    private final SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    public SchedulerFactoryConfig(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    public void schedulerJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        registerJobs(scheduler);
        scheduler.start();
    }

    public void registerJobs(Scheduler scheduler) throws SchedulerException {
        log.info("--------------------------reigsting quartz task--------------------------");
        log.info("tasks map size:{}", tasks.size());
        tasks.forEach((k, v) -> {
            log.info("clz: [" + k + " ] => cron: [" + v + "]");
            try {
                Class<?> jobClz = Class.forName(k);
                @SuppressWarnings("unchecked")
                JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobClz).build();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(v);
                CronTrigger trigger =
                        TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (ClassNotFoundException e) {
                log.error(e.toString());
            } catch (SchedulerException e) {
                log.error(e.toString());
            }
        });
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.schedulerJob();
            log.info("SynchronizedData job start...");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
