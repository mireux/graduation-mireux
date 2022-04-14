package com.example.graduationlhj.mapper;

import com.example.graduationlhj.entity.Seat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.graduationlhj.params.Vo.AdminInfoVo;
import com.example.graduationlhj.params.Vo.SeatVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhj
 * @since 2022-01-11
 */
public interface SeatMapper extends BaseMapper<Seat> {

    /**
     * 删除对应数量的座位
     * @param roomId
     * @param number
     * @return
     */
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

    /**
     *  获取对应的教室的所有的座位
     * @param roomId
     * @return
     */
    List<SeatVo> getAllList(String roomId);

}
