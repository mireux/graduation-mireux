package com.example.graduationlhj.service;

import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.graduationlhj.params.param.LoginParam;
import com.example.graduationlhj.params.param.PasswordParam;
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

    /**
     * 获取所有的用户列表
     */
    Result getAllUser();

    /**
     * 添加用户
     * @param user 用户信息
     * @return
     */
    Result InsertUser(User user);

    /**
     * 改变用户状态
     * @param id 用户id
     * @param status 用户状态
     * @return
     */
    Result changeTheStatus(Long id, String status);

    /**
     * 删除用户
     * @param id 用户id
     * @return
     */
    Result deleteUser(Long id);

    /**
     * 修改密码
     * @param passwordParam 前端回传的数据
     * @return
     */
    Result changePass(PasswordParam passwordParam);
}
