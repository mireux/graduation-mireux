package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.utils.JwtUtil;
import com.example.graduationlhj.utils.QiniuUtils;
import com.example.graduationlhj.utils.RedisCache;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("upload")
@Api("上传接口")
public class UpLoadController {

    private final QiniuUtils qiniuUtils;
    private final RedisCache redisCache;

    @Autowired
    public UpLoadController(QiniuUtils qiniuUtils, RedisCache redisCache) {
        this.qiniuUtils = qiniuUtils;
        this.redisCache = redisCache;
    }

    /**
     * 上传图片至图库 这里使用的是七牛云图库
     * @param file
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping
    @ApiOperation("上传图片至图库(七牛云)")
    public Result upload(@RequestParam MultipartFile file, @RequestHeader String token) throws Exception {
        String fileName = UUID.randomUUID() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload) {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            String ImageUrl = qiniuUtils.url + fileName;
//            System.out.println(ImageUrl);
            // 设置600s过期时间
            redisCache.setCacheObject("Avatar:" + userId,ImageUrl,10, TimeUnit.MINUTES);
            return new Result(200,"",ImageUrl);
        }
        return new Result(6001,"上传失败");
    }


}
