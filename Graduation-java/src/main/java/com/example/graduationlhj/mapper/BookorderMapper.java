package com.example.graduationlhj.mapper;

import com.example.graduationlhj.entity.Bookorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhj
 * @since 2022-01-12
 */
public interface BookorderMapper extends BaseMapper<Bookorder> {

    /**
     * 通过roomId 和 seatId 获取对应的订单
     * @param roomId
     * @param seatId
     * @return
     */
    Long searchOrder(Long roomId, Long seatId);
}
