package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.params.param.SeatBookParam;
import com.example.graduationlhj.service.SeatService;
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
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/getList")
    public Result getListById(String roomId) {
        return seatService.getListById(roomId);
    }

    @PostMapping("/book")
    public Result bookTheSeat(@RequestBody SeatBookParam seatBookParam) {
        return seatService.bookTheSeat(seatBookParam);
    }

    @PostMapping("/status/change")
    public Result changeSeatStatus(Long seatId) {
        return seatService.changeSeatStatus(seatId);
    }

    @PostMapping("/lock")
    public Result lockSeat(Long seatId) {
        System.out.println("seatId = " + seatId);
        return seatService.lockSeat(seatId);
    }

}
