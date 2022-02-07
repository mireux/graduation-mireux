package com.example.graduationlhj.filter;

import com.example.graduationlhj.common.aop.LogAnnotation;
import com.example.graduationlhj.params.LoginUser;
import com.example.graduationlhj.utils.JwtUtil;
import com.example.graduationlhj.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;

    @Autowired
    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 大致流程写一下：
     * 1. 获取token 如果没有token那么直接放行
     * 2. 解析token 如果token解析成功 从redis中获取用户信息 如果解析失败 则返回错误
     * 3. 存放用户信息到SecurityContextHolder 因为setAuthentication需要一个Authentication对象 所以我们需要进行封装
     * 4. 放行
     * 5. filter写完过后还需要在SecurityConfig设置一下，让我们手写的过滤器加入到SpringSecurity的过滤器链中
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            // 如果为空 那么直接放行
            filterChain.doFilter(request, response);
            // 因为没有token 所以不需要进行下面的解析操作 直接返回就行了
            return;
        }
        // 解析token
        String userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果token解析成功 从redis中获取用户信息 如果解析失败 则返回错误
        String redisKey = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        // 存放用户信息到SecurityContextHolder 因为setAuthentication需要一个Authentication对象 所以我们需要进行封装
        /**
         * 这里需要使用三个函数的方法 方法内有super.setAuthenticated(true); 表示已经认证
         * 可以跳过后面的一些认证
         * 获取权限信息封装到Authentication
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行去往下一个过滤器
        filterChain.doFilter(request,response);
    }
}
