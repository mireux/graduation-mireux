package com.example.graduationlhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Studyreport;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lhj
 * @since 2022-04-23
 */
public interface StudyreportService extends IService<Studyreport> {

    /**
     * 创建新用户时候创建新得学习订单
     *
     * @param adminId 用户id
     */
    Boolean createStudyReport(Long adminId);

    /**
     * 完成订单后改变学习报告中得数据
     *
     * @param bookOrderId 订单id 获取对应得订单信息
     */
    void changeStudyReport(String bookOrderId);


    /**
     * 根据用户id获取对应的学习记录
     *
     * @return
     */
    Result getStudyReportById();
}
