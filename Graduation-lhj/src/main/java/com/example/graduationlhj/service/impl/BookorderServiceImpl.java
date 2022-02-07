package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Bookorder;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.BookorderMapper;
import com.example.graduationlhj.mapper.RoomMapper;
import com.example.graduationlhj.mapper.SeatMapper;
import com.example.graduationlhj.params.param.BookOrderParam;
import com.example.graduationlhj.params.param.SeatBookParam;
import com.example.graduationlhj.service.BookorderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.graduationlhj.service.SeatService;
import com.example.graduationlhj.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhj
 * @since 2022-01-12
 */
@Service
public class BookorderServiceImpl extends ServiceImpl<BookorderMapper, Bookorder> implements BookorderService {

    private final BookorderMapper bookorderMapper;

    private final QuartzService quartzService;

    private final SeatMapper seatMapper;

    private final RoomMapper roomMapper;


    public BookorderServiceImpl(BookorderMapper bookorderMapper, QuartzService quartzService, SeatMapper seatMapper, RoomMapper roomMapper) {
        this.bookorderMapper = bookorderMapper;
        this.quartzService = quartzService;
        this.seatMapper = seatMapper;
        this.roomMapper = roomMapper;
    }

    /**
     * 创建订单
     * @param seatBookParam
     */
    @Override
    @Async
    public void createOrder(SeatBookParam seatBookParam,Long userId) {
        Bookorder bookorder = new Bookorder();
        BeanUtils.copyProperties(seatBookParam,bookorder);
        bookorder.setBookEndTime(seatBookParam.getEndTime());
        bookorder.setBookStartTime(seatBookParam.getStartTime());
        bookorder.setCreateTime(LocalDateTime.now());
        bookorder.setIsFinished(seatBookParam.getFinish());
        bookorder.setAdminId(userId);
        System.out.println(bookorder);
        if (bookorderMapper.insert(bookorder) < 0) {
            System.out.println("订单生成失败");
            return ;
        }
        // 开启定时任务
        quartzService.SeatCountDown(seatBookParam,userId,bookorder.getId());
    }

    /**
     * 查询该用户是否有未完成的订单表
     */
    public int checkOrderByFinish(Long userId) {
        LambdaQueryWrapper<Bookorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bookorder::getAdminId,userId).eq(Bookorder::getIsFinished,false);
        Integer integer = bookorderMapper.selectCount(lambdaQueryWrapper);
        return integer;
    }

    /**
     * 结束对应的订单
     * @param bookOrderId
     */
    @Override
    public void finishOrder(String bookOrderId) {
        LambdaQueryWrapper<Bookorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bookorder::getId,bookOrderId);
        Bookorder bookorder = bookorderMapper.selectOne(lambdaQueryWrapper);
        bookorder.setIsFinished(1);
        if (bookorderMapper.updateById(bookorder) < 0) {
            System.out.println("finishOrder:结束订单失败");
        }
    }

    /**
     * 获取用户的所有订单
     * @return
     */
    @Override
    public Result getAllOrder() {
        User user = UserUtils.getUserInfo();
        Long userId = user.getId();
        LambdaQueryWrapper<Bookorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bookorder::getAdminId,userId).orderByDesc(Bookorder::getCreateTime);
        List<Bookorder> bookOrders = bookorderMapper.selectList(lambdaQueryWrapper);
        List<BookOrderParam> bookOrderParams = bookOrders.stream()
                .map(this::copy)
                .collect(Collectors.toList());
        System.out.println(bookOrderParams);
        return new Result(200,"",bookOrderParams);
    }

    /**
     * 取消订单
     * @return
     */
    @Override
    public Result cancelOrder(String bookOrderId) {
        // 1. 订单状态改为3 表示取消状态
        // 2. 修改对应的座位状态为未选状态
        Long id = Long.valueOf(bookOrderId);
        LambdaQueryWrapper<Bookorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Bookorder::getId,id);
        Bookorder bookorder = bookorderMapper.selectOne(lambdaQueryWrapper);
        bookorder.setIsFinished(3);
        // 没有失败判断 偷懒了
        bookorderMapper.updateById(bookorder);
        seatMapper.releaseSeat(bookorder.getSeatId());
        return new Result(200,"取消预定成功");
    }

    private BookOrderParam copy(Bookorder bookorder) {
        BookOrderParam bookOrderParam = new BookOrderParam();
        BeanUtils.copyProperties(bookorder,bookOrderParam);
        // 通过对应的id获取到对应的信息 roomId -> room seatId -> seat
        String seat = seatMapper.getSeat(bookorder.getSeatId());
        String room = roomMapper.getRoom(bookorder.getRoomId());
        bookOrderParam.setRoom(room);
        bookOrderParam.setSeat(seat);
        return bookOrderParam;
    }

}
