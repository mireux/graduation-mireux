package com.example.graduationlhj.params.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookOrderParam {

    private Long id;

    private LocalDateTime bookEndTime;

    private LocalDateTime bookStartTime;

    private LocalDateTime createTime;

    private String room;

    private String seat;

    private Integer isFinished;
}
