package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.aop.LogAnnotation;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.params.param.LoginParam;
import com.example.graduationlhj.params.param.UserInfoParam;
import com.example.graduationlhj.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lhj
 * @since 2022-01-08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    @ApiOperation("登录操作")
    @LogAnnotation(module = "UserController", operator = "登录操作")
    public Result login(@RequestBody LoginParam loginParam, @CookieValue("captchaCode") String uuid) {
        loginParam.setUuid(uuid);
        return userService.login(loginParam);
    }

    @LogAnnotation(module = "User", operator = "获取用户信息")
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result getUserInfo() {
        return userService.getUserInfo();
    }


    @PostMapping("/logout")
    @ApiOperation("登出操作")
    public Result logout() {
        return userService.logout();
    }

    @PostMapping("/info/update")
    @ApiOperation("更新用户信息")
    @LogAnnotation(module = "User", operator = "更新用户信息操作")
    public Result UpdateUserInfoById(@RequestBody UserInfoParam userInfoParam,@RequestHeader String token) {
        return userService.updateUserInfoById(userInfoParam,token);
    }

    @PostMapping("/avatar/update")
    @ApiOperation("更新头像")
    public Result UpdateAvatarById(@RequestHeader String token) {
        return userService.updateAvatarById(token);
    }

    @PostMapping("/avatar/cancel")
    @ApiOperation("取消更新头像")
    public Result CancelUpdateAvatar(@RequestHeader String token) {
        return userService.CancelUpdateAvatar(token);
    }

}
