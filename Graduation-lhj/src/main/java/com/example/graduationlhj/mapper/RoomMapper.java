package com.example.graduationlhj.mapper;

import com.example.graduationlhj.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhj
 * @since 2022-01-10
 */
public interface RoomMapper extends BaseMapper<Room> {

    /**
     * 获取对应的room
     * @param roomId
     * @return
     */
    String getRoom(Long roomId);
}
