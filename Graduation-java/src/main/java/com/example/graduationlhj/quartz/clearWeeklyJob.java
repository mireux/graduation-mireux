package com.example.graduationlhj.quartz;

import com.example.graduationlhj.entity.Studyreport;
import com.example.graduationlhj.mapper.StudyreportMapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class clearWeeklyJob extends QuartzJobBean {

    private final StudyreportMapper studyreportMapper;

    public clearWeeklyJob(StudyreportMapper studyreportMapper) {
        this.studyreportMapper = studyreportMapper;
    }

    /**
     * 这里是先获取所有列表然后一个个更新返回数据库 这里得问题就是如果我有10w条数据 那么我就要访问10w次数据库
     * TODO 不知道有没有更好得做法
     *
     * @param context Quartz参数
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 清理表StudyReport中bookWeekTimes中的数据
        //1. 获取列表
        List<Studyreport> studyReports = studyreportMapper.selectList(null);
        //2. 更新
        studyReports.stream().forEach(studyreport -> {
            studyreport.setBookWeekTimes(0);
            studyreportMapper.updateById(studyreport);
        });
    }
}
