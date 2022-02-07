package com.example.graduationlhj.mapper;

import com.example.graduationlhj.entity.Seat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhj
 * @since 2022-01-11
 */
public interface SeatMapper extends BaseMapper<Seat> {

    boolean deleteByNumber(Long roomId, int number);

    /**
     * 获取对应的座位名
     * @param seatId
     * @return
     */
    String getSeat(Long seatId);

    /**
     * 解锁对应的seat
     * @param seatId
     */
    void releaseSeat(Long seatId);
}
