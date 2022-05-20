package com.example.graduationlhj.controller;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.service.InfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@Api("数据统计接口")
public class InfoController {

    private final InfoService infoService;


    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    /**
     * 管理员获得统计数据
     * @return
     */
    @GetMapping("/admin/get")
    @ApiOperation("获取自习室数据")
    public Result adminGetInfo() {
        return infoService.adminGetInfo();
    }

    @GetMapping("/admin/get/room")
    @ApiOperation("获取全部自习室")
    public Result adminGetInfoRoom() {
        return infoService.adminGetInfoRoom();
    }

}
