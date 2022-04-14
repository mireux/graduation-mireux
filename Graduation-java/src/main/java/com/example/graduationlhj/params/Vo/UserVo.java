package com.example.graduationlhj.params.Vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserVo {

    private Long id;

    private String userName;

    private String nickName;

    private String sex;

    private String CreateBy;

    private LocalDateTime createTime;

    private String UpdateBy;

    private String updateTime;

    private String status;

    private Integer delFlag;

}
