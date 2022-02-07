package com.example.graduationlhj.params.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeatBookParam {
    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime endTime;


    public Long roomId;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime startTime;

    public Long seatId;

    public Integer finish;

}
