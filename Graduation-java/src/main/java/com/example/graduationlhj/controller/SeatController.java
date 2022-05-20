package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.params.param.SeatBookParam;
import com.example.graduationlhj.service.SeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lhj
 * @since 2022-01-11
 */
@RestController
@RequestMapping("/seat")
@Api("管理座位接口")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/getList")
    @ApiOperation("根据id获取座位")
    public Result getListById(@Parameter(description = "教师id") String roomId) {
        return seatService.getListById(roomId);
    }

    @PostMapping("/book")
    @ApiOperation("预定座位")
    public Result bookTheSeat(@Parameter(description = "前端回传的预定参数") @RequestBody SeatBookParam seatBookParam) {
        return seatService.bookTheSeat(seatBookParam);
    }

    @PostMapping("/status/change")
    @ApiOperation("改变座位状态")
    public Result changeSeatStatus(@Parameter(description = "座位Id") Long seatId) {
        return seatService.changeSeatStatus(seatId);
    }

    @PostMapping("/lock")
    @ApiOperation("锁座/解座")
    public Result lockSeat(@Parameter(description = "座位Id") Long seatId) {
        return seatService.lockSeat(seatId);
    }

}
