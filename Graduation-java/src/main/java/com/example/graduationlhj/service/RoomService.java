package com.example.graduationlhj.service;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.graduationlhj.params.param.RoomInsertParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhj
 * @since 2022-01-10
 */
public interface RoomService extends IService<Room> {

    /**
     * 获取所有的自习室列表
     * @return
     */
    Result getAllRoom(Boolean isScreen);

    /**
     * 新添加一条自习室记录
     * @param roomInsertParam
     * @return
     */
    Result insertRoom(RoomInsertParam roomInsertParam);

    /**
     * 停用自习室
     * @param id
     * @return
     */
    Result disableRoom(String id);

    /**
     * 修改自习室信息
     * @param id
     * @return
     */
    Result editRoom(String id,RoomInsertParam roomInsertParam);
}
