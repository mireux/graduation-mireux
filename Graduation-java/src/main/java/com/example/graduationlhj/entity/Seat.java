package com.example.graduationlhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhj
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String seat;

    @TableField("roomId")
    private Long roomId;

    @TableField("delFlag")
    private Boolean delFlag;

    @TableField("isChoosed")
    private Boolean isChoosed;

    @TableField("createBy")
    private Long createBy;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("updateBy")
    private Long updateBy;

    @TableField("updateTime")
    private LocalDateTime updateTime;


}
