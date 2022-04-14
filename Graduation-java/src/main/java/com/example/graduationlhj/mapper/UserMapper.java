package com.example.graduationlhj.mapper;

import com.example.graduationlhj.entity.Role;
import com.example.graduationlhj.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.graduationlhj.params.Vo.UserVo;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lhj
 * @since 2022-01-08
 */
public interface UserMapper extends BaseMapper<User> {


    /**
     * 根据Id获取权限
     * @param id
     * @return
     */
    Role getRoleByUserId(Long id);

    /**
     * 根据Id更新用户头像
     * @param imageUrl
     */
    int updateAvatarByUserId(String imageUrl,Long id);

    /**
     * 根据用户id获取到对应的userName
     * @param userId
     * @return
     */
    String getUserNameById(Long userId);

    /**
     * 获取所有的用户
     * @return
     */
    List<UserVo> getAllUser();

}
