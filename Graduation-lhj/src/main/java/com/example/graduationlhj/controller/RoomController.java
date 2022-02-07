package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.params.param.RoomInsertParam;
import com.example.graduationlhj.service.RoomService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhj
 * @since 2022-01-10
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/getAll")
    @ApiOperation("获取所有自习室的信息列表")
    public Result getAllRoom(Boolean isScreen) {
       return roomService.getAllRoom(isScreen);
    }

    @PostMapping("/insert")
    @ApiOperation("新添加自习室记录")
    public Result insertRoom(@RequestBody RoomInsertParam roomInsertParam) {
        return roomService.insertRoom(roomInsertParam);
    }


    @PostMapping("/disabled")
    @ApiOperation("删除自习室记录")
    public Result disabledRoom(String id) {
        return roomService.disableRoom(id);
    }

    @PostMapping("/edit")
    @ApiOperation("修改自习室记录")
    public Result editRoom(String id,@RequestBody RoomInsertParam roomInsertParam) {
        return roomService.editRoom(id,roomInsertParam);
    }

}
