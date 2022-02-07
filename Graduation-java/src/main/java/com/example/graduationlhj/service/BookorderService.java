package com.example.graduationlhj.service;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Bookorder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.graduationlhj.params.param.BookOrderParam;
import com.example.graduationlhj.params.param.SeatBookParam;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhj
 * @since 2022-01-12
 */
public interface BookorderService extends IService<Bookorder> {

    /**
     * 创建订单
     * @param seatBookParam
     * @return
     */
    void createOrder(SeatBookParam seatBookParam,Long userId);

    /**
     * 检查订单是否存在未完成
     * @param userId
     * @return
     */
    int checkOrderByFinish(Long userId);

    /**
     * 结束对应的订单
     * @param bookOrderId
     */
    void finishOrder(String bookOrderId);

    /**
     * 获取该用户的所有订单
     * @return
     */
    Result getAllOrder();

    /**
     * 取消订单
     * @param bookOrderId
     * @return
     */
    Result cancelOrder(String bookOrderId);
}
