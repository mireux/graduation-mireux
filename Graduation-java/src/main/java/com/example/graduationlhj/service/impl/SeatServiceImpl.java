package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Seat;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.SeatMapper;
import com.example.graduationlhj.mapper.UserMapper;
import com.example.graduationlhj.params.RoomInfo;
import com.example.graduationlhj.params.Vo.SeatVo;
import com.example.graduationlhj.params.param.SeatBookParam;
import com.example.graduationlhj.service.BookorderService;
import com.example.graduationlhj.service.SeatService;
import com.example.graduationlhj.utils.UserUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lhj
 * @since 2022-01-11
 */
@Service
@Transactional
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {

    private final SeatMapper seatMapper;

    private final BookorderService bookorderService;

    private final UserMapper userMapper;



    public SeatServiceImpl(SeatMapper seatMapper, BookorderService bookorderService, UserMapper userMapper) {
        this.seatMapper = seatMapper;
        this.bookorderService = bookorderService;
        this.userMapper = userMapper;
    }

    // 根据roomId创建对应数量的座位
    @Override
    @Async
    public void createSeat(Long roomId, Integer number, User user) {
        insertByList(1, number, roomId, user);
    }

    // 根据roomId修改对应数量的座位
    @Async
    public void editSeatNumber(RoomInfo roomInfo) {
        User user = UserUtils.getUserInfo();
        Long roomId = roomInfo.getRoomId();
        Integer newNumber = roomInfo.getNewNumber();
        Integer oldNumber = roomInfo.getOldNumber();
        /**
         * 这里需要进行判断
         * 如果 newNumber > oldNumber 说明是需要添加座位 进行插入操作
         * 如果 newNumber < oldNumber 说明是需要删除座位 进行删除操作
         * 如果 newNumber == oldNumber 说明相等 无需操作
         */
        int number = newNumber - oldNumber;
        if (number > 0) {
            // 批量添加操作
            // 需要先获取到原先最大的座位号 再原先的座位号上继续添加
            LambdaQueryWrapper<Seat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Seat::getRoomId, roomId).orderByDesc(Seat::getSeat).last("limit 1");
            Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
            if (seat != null) {
                String seat1 = seat.getSeat();
                int oldMaxNumber = Integer.parseInt(seat1);
                insertByList(oldMaxNumber + 1, newNumber, roomId, user);
            } else {
                insertByList(1, number, roomId, user);
            }
        } else if (number < 0) {
            // 删除操作
            // 删除操作不需要获取到原先的最大的座位号 直接排序删除 limit #{number}即可
            synchronized (this) {
                number = Math.abs(number);
                if (!seatMapper.deleteByNumber(roomId, number)) {
                    System.out.println("editSeat:删除失败");
                }
                System.out.println("editSeat:删除成功");
            }
        }
    }

    /**
     * 通过id获取到对应的座位列表
     *
     * @param roomId
     * @return
     */
    @Override
    public Result getListById(String roomId) {
        List<SeatVo> seats = seatMapper.getAllList(roomId);
        seats.forEach(seatVo -> {
//            System.out.println("seatVo = " + seatVo);
            seatVo.setCreateBy(userMapper.getUserNameById(Long.valueOf(seatVo.getCreateBy())));
            seatVo.setUpdateBy(userMapper.getUserNameById(Long.valueOf(seatVo.getUpdateBy())));
        });
        System.out.println("seats = " + seats);
        return new Result(200, "", seats);
    }

    /**
     * 预定座位
     * 1. 先查询订单表 查询是否有未完成的订单 如果有则无法再预定
     * 2. 修改座位的选择状态 从false -> true
     * 3. 创建用户的预定订单（通过线程池异步处理）
     * 4. 开启Quartz框架 进行定时任务 到了时间 改变预定订单状态为结束 座位状态选为false
     *
     * @param seatBookParam
     * @return
     */
    @Override
    public Result bookTheSeat(SeatBookParam seatBookParam) {
        // 1. 检查是否有未完成的订单 有则无法继续预定
        User user = UserUtils.getUserInfo();
        if (bookorderService.checkOrderByFinish(user.getId()) > 0) {
            return new Result(4003, "您还有未完成的订单！");
        }
        // 2. 根据seatId 获取对应的座位实体并修改它的isChoosed字段为true
        LambdaQueryWrapper<Seat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Seat::getId, seatBookParam.getSeatId());
        Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
        System.out.println(seat);
        // 并发不高就不上锁了
        // 检查一下IsChoosed是否为false 如果不为false说明已经被抢了 那么就返回
        if (seat.getIsChoosed()) {
            return new Result(4001, "预定失败");
        }
        seat.setIsChoosed(!seat.getIsChoosed());
        if (seatMapper.updateById(seat) < 0) {
            return new Result(4001, "添加失败");
        }
        // 3. 启用异步方法去创建用户的订单 传入seatBookParam
        bookorderService.createOrder(seatBookParam, user.getId());
        return new Result(200, "预定成功");
    }

    /**
     * 改变对应的座位状态
     *
     * @param seatId
     */
    @Override
    public void releaseSeat(Long seatId) {
        LambdaQueryWrapper<Seat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Seat::getId, seatId);
        Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
        seat.setIsChoosed(!seat.getIsChoosed());
        if (seatMapper.updateById(seat) < 0) {
            System.out.println("releaseSeat:插入失败");
        }
    }

    /**
     * 更改座位的状态
     *
     * @param seatId 座位id
     * @return
     */
    @Override
    public Result changeSeatStatus(Long seatId) {
        /**
         * 通过seatId获取对应的seat 然后改变座位状态
         */
        LambdaQueryWrapper<Seat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Seat::getId, seatId);
        Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
        seat.setDelFlag(!seat.getDelFlag());
        // 更新更新人和更新时间
        User user = UserUtils.getUserInfo();
        seat.setUpdateBy(user.getId());
        seat.setUpdateTime(LocalDateTime.now());
        if (seatMapper.updateById(seat) < 0) {
            return new Result(50001, "更新数据失败");
        }
        return new Result(200, "更新成功");
    }

    /**
     * 锁定座位或者解除锁定
     *
     * @param seatId
     * @return
     */
    @Override
    public Result lockSeat(Long seatId) {
        /**
         * 通过seatId获取对应的seat 然后改变座位状态
         */
        LambdaQueryWrapper<Seat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Seat::getId, seatId);
        Seat seat = seatMapper.selectOne(lambdaQueryWrapper);
        if (seat.getIsChoosed()) {
            // 改变订单的状态
            // 通过seat 找到对应的订单 解除订单
            Long id = seat.getId();
            Long roomId = seat.getRoomId();
            Long bookOrderId = bookorderService.searchOrder(roomId, id);
            bookorderService.finishOrder(bookOrderId.toString());
        }
        seat.setIsChoosed(!seat.getIsChoosed());
        // 更新更新人和更新时间
        User user = UserUtils.getUserInfo();
        seat.setUpdateBy(user.getId());
        seat.setUpdateTime(LocalDateTime.now());
        if (seatMapper.updateById(seat) < 0) {
            return new Result(50001, "更新数据失败");
        }
        return new Result(200, "更新成功");
    }

    // 批量插入
    public void insertByList(int oldMaxNumber, int newMaxNumber, Long roomId, User user) {
        synchronized (this) {
            // 批量添加
            List<Seat> list = new ArrayList<>();
            for (int i = oldMaxNumber; i <= newMaxNumber; i++) {
                Seat seat = new Seat();
                seat.setDelFlag(false);
                seat.setIsChoosed(false);
                seat.setRoomId(roomId);
                seat.setCreateBy(user.getId());
                seat.setCreateTime(LocalDateTime.now());
                seat.setUpdateBy(user.getId());
                seat.setUpdateTime(LocalDateTime.now());
                seat.setSeat(i < 10 ? "0" + i : String.valueOf(i));
                list.add(seat);
            }
            if (!this.saveBatch(list)) {
                System.out.println("添加座位失败");
            } else {
                System.out.println("添加座位成功");
            }
        }
    }
}
