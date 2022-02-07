package com.example.graduationlhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhj
 * @since 2022-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String room;

    /**
     * 可以容纳的人数
     */
    @TableField("number")
    private Integer number;

    @TableField("createBy")
    private Long createBy;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("updateBy")
    private Long updateBy;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("delFlag")
    private Boolean delFlag;


}
