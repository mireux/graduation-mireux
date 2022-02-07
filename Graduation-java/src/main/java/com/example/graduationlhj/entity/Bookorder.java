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
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Bookorder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("roomId")
    private Long roomId;

    @TableField("seatId")
    private Long seatId;

    @TableField("adminId")
    private Long adminId;

    @TableField("bookStartTime")
    private LocalDateTime bookStartTime;

    @TableField("bookEndTime")
    private LocalDateTime bookEndTime;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("isFinished")
    private Integer isFinished;


}
