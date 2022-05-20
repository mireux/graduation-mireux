package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.service.StudyreportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lhj
 * @since 2022-04-23
 */
@RestController
@RequestMapping("/studyreport")
@Api("学习报告接口")
public class StudyreportController {

    private final StudyreportService studyreportService;


    public StudyreportController(StudyreportService studyreportService) {
        this.studyreportService = studyreportService;
    }

    @GetMapping("/get")
    @ApiOperation("根据id获取学习报告")
    public Result getStudyReportById() {
        return studyreportService.getStudyReportById();
    }


}
