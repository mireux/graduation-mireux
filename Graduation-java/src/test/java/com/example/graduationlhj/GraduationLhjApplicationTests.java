package com.example.graduationlhj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.graduationlhj.entity.Room;
import com.example.graduationlhj.entity.Seat;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.MenuMapper;
import com.example.graduationlhj.mapper.RoomMapper;
import com.example.graduationlhj.mapper.SeatMapper;
import com.example.graduationlhj.mapper.UserMapper;
import com.example.graduationlhj.params.RoomInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@SpringBootTest
class GraduationLhjApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Test
    public void TestUserMapper() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,"lhj");
        User user = userMapper.selectOne(lambdaQueryWrapper);
        System.out.println(user);
    }

    @Test
    public void TestMenuMapper() {
        List<String> list = menuMapper.selectPermsByUserId(2L);
        list.stream()
                .distinct()
                .forEach(System.out::println);
    }


    @Test
    public void TestSplitString() {
        String key = "http://r5hf5263q.hd-bkt.clouddn.com/8ea51ba7-37d7-48a0-bc48-3f7f104107f5.jpg";
        String[] split = key.split("/");
        System.out.println(split[3]);
        Date date = new Date();
        System.out.println(date.getTime());
    }

    @Test
    public void TestRoomMapper() {
        List<Room> rooms = roomMapper.selectList(null);
        System.out.println(rooms);
    }

    @Test
    public void TestSeatMapper() {
        Long roomId = 13L;
        LambdaQueryWrapper<Seat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Seat::getRoomId,roomId).orderByDesc(Seat::getSeat).last("limit 1");
        Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
        System.out.println(seat.getSeat());
//        System.out.println(integer);
    }

}
