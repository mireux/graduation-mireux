package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Bookorder;
import com.example.graduationlhj.entity.Studyreport;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.BookorderMapper;
import com.example.graduationlhj.mapper.StudyreportMapper;
import com.example.graduationlhj.params.Vo.StudyReportVo;
import com.example.graduationlhj.service.StudyreportService;
import com.example.graduationlhj.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lhj
 * @since 2022-04-23
 */
@Service
@Slf4j
public class StudyreportServiceImpl extends ServiceImpl<StudyreportMapper, Studyreport> implements StudyreportService {

    private final StudyreportMapper studyreportMapper;

    private final BookorderMapper bookorderMapper;

    public StudyreportServiceImpl(StudyreportMapper studyreportMapper, BookorderMapper bookorderMapper) {
        this.studyreportMapper = studyreportMapper;
        this.bookorderMapper = bookorderMapper;
    }


    /**
     * 创建新的学习报告记录
     *
     * @param adminId 用户id
     */
    @Override
    public Boolean createStudyReport(Long adminId) {
        Studyreport studyreport = new Studyreport();
        studyreport.setAdminId(adminId);
        studyreport.setBookTotalTimes(0);
        studyreport.setTotalStudyTime(0);
        studyreport.setLastMonthStudyTime(0);
        studyreport.setBookWeekTimes(0);
        if (studyreportMapper.insert(studyreport) < 0) {
            log.info("创建学习记录失败");
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void changeStudyReport(String bookOrderId) {
        LambdaQueryWrapper<Bookorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bookorder::getId, bookOrderId);
        Bookorder bookorder = bookorderMapper.selectOne(lambdaQueryWrapper);
        Long adminId = bookorder.getAdminId();// 用户id
        LocalDateTime bookStartTime = bookorder.getBookStartTime(); // 开始时间
        LocalDateTime bookEndTime = bookorder.getBookEndTime(); // 结束时间
        // 计算对应的学习时长
        Duration duration = Duration.between(bookStartTime, bookEndTime);
        long min = duration.toMinutes();
        // 根据adminId找到对应的学习记录
        LambdaQueryWrapper<Studyreport> StudyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        StudyLambdaQueryWrapper.eq(Studyreport::getAdminId, adminId);
        Studyreport studyreport = studyreportMapper.selectOne(StudyLambdaQueryWrapper);
        studyreport.setBookTotalTimes(studyreport.getBookTotalTimes() + 1);
        studyreport.setTotalStudyTime(studyreport.getTotalStudyTime() + (int) min);
        studyreport.setBookWeekTimes(studyreport.getBookWeekTimes() + 1);
        studyreport.setLastMonthStudyTime(studyreport.getLastMonthStudyTime() + (int) min);
        if (studyreportMapper.updateById(studyreport) < 0) {
            log.info("更新学习记录失败");
        }
    }

    /**
     * 根据id获取对应得学习记录
     *
     * @return
     */
    @Override
    public Result getStudyReportById() {
        User userInfo = UserUtils.getUserInfo();
        LambdaQueryWrapper<Studyreport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Integer totalNumber = studyreportMapper.selectCount(null);
        lambdaQueryWrapper.eq(Studyreport::getAdminId, userInfo.getId());
        Studyreport studyreport = studyreportMapper.selectOne(lambdaQueryWrapper);
        StudyReportVo studyReportVo = new StudyReportVo();
        BeanUtils.copyProperties(studyreport, studyReportVo);
        // 获取所有小于个人的预约的人数
        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(Studyreport::getBookTotalTimes, studyreport.getBookTotalTimes());
        studyReportVo.setTotalTimesRating(studyreportMapper.selectCount(lambdaQueryWrapper) / Double.valueOf(totalNumber));
        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(Studyreport::getBookWeekTimes, studyreport.getBookWeekTimes());
        studyReportVo.setWeekTimesRating(studyreportMapper.selectCount(lambdaQueryWrapper) / Double.valueOf(totalNumber));
        return new Result(200, "", studyReportVo);
    }
}
