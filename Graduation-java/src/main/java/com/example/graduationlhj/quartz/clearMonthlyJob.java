package com.example.graduationlhj.quartz;

import com.example.graduationlhj.entity.Studyreport;
import com.example.graduationlhj.mapper.StudyreportMapper;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class clearMonthlyJob extends QuartzJobBean {

    private final StudyreportMapper studyreportMapper;

    public clearMonthlyJob(StudyreportMapper studyreportMapper) {
        this.studyreportMapper = studyreportMapper;
    }

    //跟周清除同理
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 清理表StudyReport中bookWeekTimes中的数据
        //1. 获取列表
        List<Studyreport> studyReports = studyreportMapper.selectList(null);
        //2. 更新
        studyReports.stream().forEach(studyreport -> {
            studyreport.setLastMonthStudyTime(0);
            studyreportMapper.updateById(studyreport);
        });
    }
}
