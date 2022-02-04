package com.example.application.quartz.basics.scheduler;

import com.example.application.quartz.basics.service.NewCurrencyRatesJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewCurrencyRatesJob implements Job {

    @Autowired
    private NewCurrencyRatesJobService jobService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("Job ** " + context.getJobDetail().getKey().getName() + " " + context.getJobDetail().getKey() + " ** fired @ " + context.getFireTime());

        jobService.executeSampleJob("");

        System.out.println("Next job scheduled @ " +context.getNextFireTime());
    }
}
