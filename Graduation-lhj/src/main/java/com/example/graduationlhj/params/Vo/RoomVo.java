package com.example.graduationlhj.params.Vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomVo {
    public Long id;

    public String room;

    private Integer number;

    public String createBy;

    public LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    /**
     * 1表示弃用或者删除
     * 0表示可用
     */
    private Boolean delFlag;
}
