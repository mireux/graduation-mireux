package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Room;
import com.example.graduationlhj.mapper.RoomMapper;
import com.example.graduationlhj.mapper.SeatMapper;
import com.example.graduationlhj.params.Vo.AdminInfoVo;
import com.example.graduationlhj.params.Vo.SeatVo;
import com.example.graduationlhj.service.InfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InfoServiceImpl implements InfoService {

    private final SeatMapper seatMapper;

    private final RoomMapper roomMapper;

    public InfoServiceImpl(SeatMapper seatMapper, RoomMapper roomMapper) {
        this.seatMapper = seatMapper;
        this.roomMapper = roomMapper;
    }


    @Override
    public Result adminGetInfo() {
        LambdaQueryWrapper<Room> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        List<Room> rooms = roomMapper.selectList(lambdaQueryWrapper.orderByDesc(Room::getRoom));
        List<Integer> chosenSeat = new ArrayList<>();
        List<Integer> totalSeat = new ArrayList<>();
        HashMap<String, List<Integer>> hashMap = new HashMap<>();
        rooms.stream().forEach(room -> {
            String roomId = room.getId().toString();
            List<SeatVo> allList = seatMapper.getAllList(roomId);
            long count = allList.stream()
                    .filter(SeatVo::getIsChoosed).count();
            chosenSeat.add((int) count);
            totalSeat.add(room.getNumber());
        });
        hashMap.put("chosenSeat",chosenSeat);
        hashMap.put("totalSeat",totalSeat);

        return new Result(200,"",hashMap);
    }

    @Override
    public Result adminGetInfoRoom() {
        LambdaQueryWrapper<Room> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        List<Room> rooms = roomMapper.selectList(lambdaQueryWrapper.orderByDesc(Room::getRoom));
        List<String> collect = rooms.stream().map(Room::getRoom).collect(Collectors.toList());
        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("room",collect);
        System.out.println("hashMap = " + hashMap);
        return new Result(200,"",hashMap);
    }
}
