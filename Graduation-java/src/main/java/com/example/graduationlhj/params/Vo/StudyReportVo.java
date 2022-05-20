package com.example.graduationlhj.params.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyReportVo {

    /**
     * 存储的是分钟数
     */
    private Integer totalStudyTime;

    /**
     * 预约次数
     */
    private Integer bookTotalTimes;

    /**
     * 上一周的预约次数
     */
    private Integer bookWeekTimes;

    /**
     * 上一个月学习总计学习时间
     */
    private Integer lastMonthStudyTime;

    /**
     * 总共次数的比例
     */
    private Double totalTimesRating;

    /**
     * 上周预约的比例
     */
    private Double weekTimesRating;
}
