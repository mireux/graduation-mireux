package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.aop.LogAnnotation;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.params.param.LoginParam;
import com.example.graduationlhj.params.param.PasswordParam;
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


    @GetMapping("/getAll")
    @ApiOperation("获取所有的用户列表")
    @LogAnnotation(module = "User", operator = "获取所有的用户")
    public Result getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/insert")
    @ApiOperation("添加用户")
    public Result InsertUser(@RequestBody User user) {
        return userService.InsertUser(user);
    }

    @PostMapping("/status/change")
    @ApiOperation("修改用户状态")
    public Result changeTheStatus(Long id,String status) {
        return userService.changeTheStatus(id,status);
    }

    @PostMapping("/delete")
    @ApiOperation("删除用户")
    public Result deleteUser(Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/changePass")
    @ApiOperation("修改密码")
    public Result changeThePassword(@RequestBody PasswordParam passwordParam) {
        return userService.changePass(passwordParam);
    }


}
