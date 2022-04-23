package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.service.StudyreportService;
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
public class StudyreportController {

    private final StudyreportService studyreportService;


    public StudyreportController(StudyreportService studyreportService) {
        this.studyreportService = studyreportService;
    }

    @GetMapping("/get")
    public Result getStudyReportById() {
        return studyreportService.getStudyReportById();
    }


}
