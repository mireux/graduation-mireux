package com.example.graduationlhj.service;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Seat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.graduationlhj.params.RoomInfo;
import com.example.graduationlhj.params.param.SeatBookParam;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lhj
 * @since 2022-01-11
 */
public interface SeatService extends IService<Seat> {

    /**
     * 创建座位
     * @param roomId
     * @param number
     */
    void createSeat(Long roomId,Integer number);

    /**
     * 修改座位容量的大小
     * @param roomInfo
     */
    void editSeatNumber(RoomInfo roomInfo);

    /**
     * 获取对应roomId的座位列表
     * @param roomId
     * @return
     */
    Result getListById(String roomId);

    /**
     * 预定座位
     * @param seatBookParam
     * @return
     */
    Result bookTheSeat(SeatBookParam seatBookParam);

    /**
     * 改变对应的座位状态
     * @param seatId
     */
    void releaseSeat(Long seatId);
}
