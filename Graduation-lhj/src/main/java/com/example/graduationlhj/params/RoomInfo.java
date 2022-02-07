package com.example.graduationlhj.params;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomInfo implements Serializable {
    private Integer oldNumber;

    private Integer newNumber;

    private Long roomId;
}
