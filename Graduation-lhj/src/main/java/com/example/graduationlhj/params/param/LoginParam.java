package com.example.graduationlhj.params.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 接受前端传到后端的登录参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam {

    // 账号
    private String username;
    // 密码
    private String password;
    // 验证码
    private String code;
    // uuid 用来验证验证码
    private String uuid;
}
