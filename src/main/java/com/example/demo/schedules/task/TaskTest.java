package com.example.demo.schedules.task;

import com.example.demo.config.aspect.annotation.LogForTimeConsumer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskTest {

    @LogForTimeConsumer//记录方法执行时间
    @Scheduled(cron = "0/5 * 7-22 * * ?")//每天7点到22点5秒钟执行一次
    public void circle(){
        System.err.println("//每天7点到22点5秒钟执行一次");
    }

    @LogForTimeConsumer//记录方法执行时间
    @Scheduled(cron = "0 40 22 * * ?")//每天22点40分执行该定时任务
    public void fixedTimes(){
        System.err.println("//每天22点40分执行该定时任务");
    }
}
