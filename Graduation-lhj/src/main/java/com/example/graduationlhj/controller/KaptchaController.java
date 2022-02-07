package com.example.graduationlhj.controller;


import com.example.graduationlhj.utils.RedisCache;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
public class KaptchaController {

    private final Producer captchaProducer;

    private final RedisCache redisCache;

    @Autowired
    public KaptchaController(Producer captchaProducer, RedisCache redisCache) {
        this.captchaProducer = captchaProducer;
        this.redisCache = redisCache;
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
}
