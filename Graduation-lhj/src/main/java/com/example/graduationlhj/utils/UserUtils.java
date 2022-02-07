package com.example.graduationlhj.utils;


import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.params.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static User getUserInfo() {
        // 从SecurityContextHolder中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户的基本信息
        User user = loginUser.getUser();
        return user;
    }
}
