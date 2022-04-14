package com.example.graduationlhj.service;


import com.example.graduationlhj.common.lang.Result;

public interface InfoService {
    /**
     * 管理员获得统计图表的数据
     * @return
     */
    Result adminGetInfo();

    /**
     * room
     * @return
     */
    Result adminGetInfoRoom();
}
