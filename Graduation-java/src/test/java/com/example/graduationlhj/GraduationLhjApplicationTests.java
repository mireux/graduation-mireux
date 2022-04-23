package com.example.graduationlhj;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.graduationlhj.entity.Bookorder;
import com.example.graduationlhj.entity.Room;
import com.example.graduationlhj.entity.Seat;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private BookorderMapper bookorderMapper;

    @Test
    public void TestUserMapper() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, "lhj");
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
        lambdaQueryWrapper.eq(Seat::getRoomId, roomId).orderByDesc(Seat::getSeat).last("limit 1");
        Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
        System.out.println(seat.getSeat());
//        System.out.println(integer);
    }

    @Test
    public void TestTimeDuration() {
        LambdaQueryWrapper<Bookorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bookorder::getId, 25L);
        Bookorder bookorder = bookorderMapper.selectOne(lambdaQueryWrapper);
        Long adminId = bookorder.getAdminId();// 用户id
        LocalDateTime bookStartTime = bookorder.getBookStartTime(); // 开始时间
        LocalDateTime bookEndTime = bookorder.getBookEndTime(); // 结束时间
        Duration duration = Duration.between(bookStartTime, bookEndTime);
        long min = duration.toMinutes();
    }


}
