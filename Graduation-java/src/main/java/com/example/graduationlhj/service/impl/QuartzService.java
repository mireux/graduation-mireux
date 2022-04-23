package com.example.graduationlhj.service.impl;

import com.example.graduationlhj.params.param.SeatBookParam;
import com.example.graduationlhj.quartz.clearMonthlyJob;
import com.example.graduationlhj.quartz.clearWeeklyJob;
import com.example.graduationlhj.quartz.countDownJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuartzService {

    private final Scheduler scheduler;

    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    // 开启定时任务 当用户预约结束后 将座位得状态修改为未选 将用户的预定改为结束
    @Async
    public void SeatCountDown(SeatBookParam seatBookParam, Long userId,Long bookOrderId) {
        LocalDateTime endTime = seatBookParam.getEndTime();
        Long seatId = seatBookParam.getSeatId();
        JobDetail jobDetail = JobBuilder.newJob(countDownJob.class)
                .usingJobData("seatId", seatId.toString())
                .usingJobData("userId", userId.toString())
                .usingJobData("bookOrderId",bookOrderId.toString())
                .build();
        // 创建触发器
        String endCron = String.format("%d %d %d %d %d ? %d", endTime.getSecond(), endTime.getMinute(), endTime.getHour(), endTime.getDayOfMonth(), endTime.getMonth().getValue(), endTime.getYear());
        CronTrigger endCronTrigger = TriggerBuilder.newTrigger()
                // 指定触发器组名和触发器名
                .withIdentity(endTime.toString(),
                        userId + "_end")
                .withSchedule(CronScheduleBuilder.cronSchedule(endCron))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, endCronTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("定时器设置失败");
        }
    }

    // 开启定时任务 每周一定时清理数据库中得周记录
    @Bean
    public void clearStudyReportWeekly() {
        String cron = " 0 0 0 ? * MON";
        JobDetail jobDetail = JobBuilder.newJob(clearWeeklyJob.class)
                .build();
        CronTrigger WeeklyTrigger = TriggerBuilder.newTrigger()
                // 指定触发器组名和触发器名
                .withIdentity("ClearWeekly",
                        "ClearWeekly")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, WeeklyTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("定时器设置失败");
        }
    }

    // 开启定时任务 每月定时清理数据库中得月学习记录
    @Bean
    public void clearStudyReportMonthly() {
        String cron = " 59 59 23 L * ?";
        JobDetail jobDetail = JobBuilder.newJob(clearMonthlyJob.class)
                .build();
        CronTrigger MonthlyTrigger = TriggerBuilder.newTrigger()
                // 指定触发器组名和触发器名
                .withIdentity("ClearMonthly",
                        "ClearMonthly")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, MonthlyTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("定时器设置失败");
        }
    }


}
