package com.example.graduationlhj.controller;


import com.example.graduationlhj.params.LoginUser;
import com.example.graduationlhj.utils.JwtUtil;
import com.example.graduationlhj.utils.RedisCache;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class KaptchaController {

    private final Producer captchaProducer;

    private final RedisCache redisCache;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public KaptchaController(Producer captchaProducer, RedisCache redisCache, AuthenticationManager authenticationManager) {
        this.captchaProducer = captchaProducer;
        this.redisCache = redisCache;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/captcha")
    @ApiOperation("验证码")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);// 禁止server端缓存
        // 设置标准的 HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // 设置IE扩展 HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");// 设置标准 HTTP/1.0 不缓存图片
        response.setContentType("image/jpeg");// 返回一个 jpeg 图片，默认是text/html(输出文档的MIMI类型)
        String capText = captchaProducer.createText();// 为图片创建文本

        // 每个用户生成uuid 防止并发访问时出现问题
        String uuid = String.valueOf(UUID.randomUUID());
        redisCache.setCacheObject(uuid, capText, 60 * 10, TimeUnit.SECONDS); // 过期时间600s
        Cookie cookie = new Cookie("captchaCode", uuid);
        response.addCookie(cookie);


        BufferedImage bi = captchaProducer.createImage(capText); // 创建带有文本的图片
        ServletOutputStream out = response.getOutputStream();
        // 图片数据输出
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    @GetMapping("/user/jwt")
    public String getJwtByTest() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("student", "123456");
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert authenticate != null;
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 获取用户ID去生成一个token
        String userId = loginUser.getUser().getId().toString();
        // 生成token
        String token = JwtUtil.createJWT(userId);
        //将数据存入redis 根据Id查找和放入
        redisCache.setCacheObject("login:" + userId, loginUser, 1, TimeUnit.DAYS);
        log.info("token: {}",token);
        return token;
    }

}
