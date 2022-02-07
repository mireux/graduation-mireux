package com.example.graduationlhj.handler;

import com.alibaba.fastjson.JSON;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 处理异常
        WebUtils.renderString(response,JSON.toJSONString(new Result(HttpStatus.UNAUTHORIZED.value(),"认证失败请查询登录")));
    }
}
