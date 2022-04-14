package com.example.graduationlhj.service.impl;

import com.example.graduationlhj.params.param.SeatBookParam;
import com.example.graduationlhj.quartz.countDownJob;
import org.quartz.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
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

    // 开启定时任务 每周一定时清除
    public void clearData() {
        // 创建触发器
        String clearCron = "0 0 0 ? * MON";
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("clearData", "clear")
                .withSchedule(CronScheduleBuilder.cronSchedule(clearCron))
                .build();
        try {
            scheduler.scheduleJob(null, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("定时器设置失败");
        }
    }
}
