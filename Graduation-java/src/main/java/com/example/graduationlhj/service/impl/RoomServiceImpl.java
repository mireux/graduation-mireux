package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Room;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.RoomMapper;
import com.example.graduationlhj.mapper.UserMapper;
import com.example.graduationlhj.params.RoomInfo;
import com.example.graduationlhj.params.Vo.RoomVo;
import com.example.graduationlhj.params.param.RoomInsertParam;
import com.example.graduationlhj.service.RoomService;
import com.example.graduationlhj.service.SeatService;
import com.example.graduationlhj.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lhj
 * @since 2022-01-10
 */
@Service
@Transactional
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    private final RoomMapper roomMapper;

    private final UserMapper userMapper;

    private final SeatService seatService;

    @Autowired
    public RoomServiceImpl(RoomMapper roomMapper, UserMapper userMapper, SeatService seatService) {
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
        this.seatService = seatService;
    }

    /**
     * 获取所有的自习室列表
     */
    @Override
    public Result getAllRoom(Boolean isScreen) {
        LambdaQueryWrapper<Room> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(isScreen) {
            // 需要进行筛选
            lambdaQueryWrapper.eq(Room::getDelFlag,0);
        }
        List<Room> rooms = roomMapper.selectList(lambdaQueryWrapper);
        // 将room -> roomVo 函数式编程
        List<RoomVo> roomVoList = rooms.stream()
                .map(this::CopyToVo)
                .collect(Collectors.toList());

        return new Result(200, "", roomVoList);
    }

    /**
     * 插入一条新的自习室记录
     *
     * @param roomInsertParam
     * @return
     */
    @Override
    public Result insertRoom(RoomInsertParam roomInsertParam) {
        User user = UserUtils.getUserInfo();
        Room room = new Room();
        room.setRoom(roomInsertParam.getName());
        room.setNumber(roomInsertParam.getCapacity());
        room.setCreateBy(user.getId());
        room.setCreateTime(LocalDateTime.now());
        room.setUpdateBy(user.getId());
        room.setUpdateTime(LocalDateTime.now());
        room.setDelFlag(false);
        if (roomMapper.insert(room) < 0) {
            return new Result(4000, "添加失败");
        }
        // 这里开启线程池使用异步处理 但是增加了异步处理之后事务失效 暂时好像无法解决
        // 用消息队列可以的确认机制可以解决这个问题 但是我是单系统没必要挂消息队列 所以暂时就这样
        seatService.createSeat(room.getId(), room.getNumber(),user);
        return new Result(200, "添加成功");
    }

    /**
     * 停用自习室
     * 其实就是删除
     * 这里是逻辑上的删除
     * 但不是删除真实数据
     * 1. 先根据Id查询到对应的room数据
     * 2. 修改room的delFlag updateBy updateTime
     * 3. 更新进数据库
     *
     * @param id
     * @return
     */
    @Override
    public Result disableRoom(String id) {
        User user = UserUtils.getUserInfo();
        Long roomId = Long.valueOf(id);
        LambdaQueryWrapper<Room> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Room::getId, roomId);
        // 获取对应的room信息
        Room room = roomMapper.selectOne(lambdaQueryWrapper);
        // 修改对应的room的delFlag
        room.setDelFlag(!room.getDelFlag());
        room.setUpdateBy(user.getId());
        room.setUpdateTime(LocalDateTime.now());
        // 写入数据库
        if (roomMapper.updateById(room) < 0) {
            return new Result(4001, "更新失败");
        }
        return new Result(200, "更新成功");
    }

    /**
     * 修改自习室的信息
     *
     * @param id
     * @return
     */
    @Override
    public Result editRoom(String id, RoomInsertParam roomInsertParam) {
        Long roomId = Long.valueOf(id);
        User user = UserUtils.getUserInfo();
        LambdaQueryWrapper<Room> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Room::getId, roomId);
        // 获取对应的room信息
        Room room = roomMapper.selectOne(lambdaQueryWrapper);
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomId(roomId);
        roomInfo.setOldNumber(room.getNumber());
        roomInfo.setNewNumber(roomInsertParam.getCapacity());
        room.setRoom(roomInsertParam.getName());
        room.setNumber(roomInsertParam.getCapacity());
        room.setUpdateBy(user.getId());
        room.setUpdateTime(LocalDateTime.now());
        // 写入数据库
        if (roomMapper.updateById(room) < 0) {
            return new Result(4001, "更新失败");
        }

        // 交换机:seat 路由:createSeat 需要两次之间number之间的变化同时还需要roomId
//         rabbitTemplate.convertAndSend("seat","editSeat",roomInfo);
        seatService.editSeatNumber(roomInfo);
        return new Result(200, "更新成功");
    }

    /**
     * 将room -> roomVo
     *
     * @param room
     * @return
     */
    public RoomVo CopyToVo(Room room) {
        RoomVo roomVo = new RoomVo();
        BeanUtils.copyProperties(room, roomVo);
        // 根据UserId获取到对应的用户的userName
        Long createBy = room.getCreateBy();
        Long updateBy = room.getUpdateBy();
        String userName = userMapper.getUserNameById(createBy);
        roomVo.setCreateBy(userName);
        userName = userMapper.getUserNameById(updateBy);
        roomVo.setUpdateBy(userName);
        return roomVo;
    }
}
