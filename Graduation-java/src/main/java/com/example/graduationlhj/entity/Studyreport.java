package com.example.graduationlhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lhj
 * @since 2022-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Studyreport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 存储的是分钟数
     */
    @TableField("totalStudyTime")
    private Integer totalStudyTime;

    /**
     * 预约次数
     */
    @TableField("bookTotalTimes")
    private Integer bookTotalTimes;

    /**
     * 用户id
     */
    @TableField("adminId")
    private Long adminId;

    /**
     * 上一周的预约次数
     */
    @TableField("bookWeekTimes")
    private Integer bookWeekTimes;

    /**
     * 上一个月学习总计学习时间
     */
    @TableField("lastMonthStudyTime")
    private Integer lastMonthStudyTime;


}
