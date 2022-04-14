package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.mapper.MenuMapper;
import com.example.graduationlhj.params.LoginUser;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
/**
 * 重写SpringSecurity中的UserDetailsService方法
 * 从原来从内存中寻找数据变成从数据库中寻找数据
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    private final MenuMapper menuMapper;

    public UserDetailsServiceImpl(UserMapper userMapper, MenuMapper menuMapper) {
        this.userMapper = userMapper;
        this.menuMapper = menuMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找id
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,username);
        lambdaQueryWrapper.eq(User::getDelFlag,0);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if(Objects.isNull(user)) {
            throw new RuntimeException("登录失败");
        }
        /*
             根据用户查询权限信息 添加到LoginUser中
             一个是List<String> permKey 表示该权限可以访问的接口
             主要是后端鉴权使用的
         */
        List<String> permission = menuMapper.selectPermsByUserId(user.getRoleId());
        // 封装成UserDetails返回
        return new LoginUser(user,permission);
    }
}
