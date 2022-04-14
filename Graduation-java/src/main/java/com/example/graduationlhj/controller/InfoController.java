package com.example.graduationlhj.controller;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.service.InfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
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
    public Result adminGetInfo() {
        return infoService.adminGetInfo();
    }

    @GetMapping("/admin/get/room")
    public Result adminGetInfoRoom() {
        return infoService.adminGetInfoRoom();
    }

}
