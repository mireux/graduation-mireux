package com.example.graduationlhj.quartz;

import com.example.graduationlhj.service.BookorderService;
import com.example.graduationlhj.service.SeatService;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class countDownJob extends QuartzJobBean {

    private final Scheduler scheduler;

    private final SeatService seatService;

    private final BookorderService bookorderService;

    public countDownJob(Scheduler scheduler, SeatService seatService, BookorderService bookorderService) {
        this.scheduler = scheduler;
        this.seatService = seatService;
        this.bookorderService = bookorderService;
    }


    @Override
    @Transactional
    protected void executeInternal(JobExecutionContext job) {
        Trigger trigger = job.getTrigger();
        JobDetail jobDetail = job.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        // 将添加任务的时候存进去的数据拿出来
        String seatId = jobDataMap.getString("seatId");
        String bookOrderId = jobDataMap.getString("bookOrderId");
        String userId = jobDataMap.getString("userId");
        // 编写任务的逻辑
        /**
         * 1. 改变座位状态变为可选状态
         * 2. 改变订单状态改变为结束状态
         * 3. 改变学习报告中的学习时长和预约次数
         */
        // 1. 改变座位的状态变为可选状态
        seatService.releaseSeat(Long.valueOf(seatId));
        // 2， 改变订单状态为结束状态
        bookorderService.finishOrder(bookOrderId);
        // 3. TODO 改变学习报告中的学习时长和预约次数
        System.out.println("改变学习报告中的时长和次数成功");
        try {
            // 暂停触发器的计时
            scheduler.pauseTrigger(trigger.getKey());
            // 移除触发器中的任务
            scheduler.unscheduleJob(trigger.getKey());
            // 删除任务
            scheduler.deleteJob(jobDetail.getKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
