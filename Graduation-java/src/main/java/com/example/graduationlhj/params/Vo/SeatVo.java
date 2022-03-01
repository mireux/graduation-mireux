package com.example.graduationlhj.params.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatVo {
    private Long id;

    private String roomName;

    private String seat;

    private Boolean delFlag;

    private Boolean isChoosed;

    private String createBy;

    private LocalDateTime createTime;

    private String UpdateBy;

    private LocalDateTime updateTime;
}
