package com.example.graduationlhj.service;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.graduationlhj.params.param.LoginParam;
import com.example.graduationlhj.params.param.UserInfoParam;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lhj
 * @since 2022-01-08
 */
public interface UserService extends IService<User> {


    /**
     * 登录操作
     */
    Result login(LoginParam loginParam);

    /**
     * 登出操作
     */
    Result logout();

    /**
     * 获取用户信息
     */
    Result getUserInfo();

    /**
     * 更新用户操作
     */
    Result updateUserInfoById(UserInfoParam userInfoParam,String token);

    /**
     * 更新用户头像操作
     */
    Result updateAvatarById(String token);

    /**
     * 取消上传头像
     */
    Result CancelUpdateAvatar(String token);
}
