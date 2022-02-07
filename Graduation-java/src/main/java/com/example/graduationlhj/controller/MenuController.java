package com.example.graduationlhj.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhj
 * @since 2022-01-09
 */
@RestController
public class MenuController {

    @GetMapping("/test/list")
    @PreAuthorize("hasAuthority('stu:seat:findAll')")
    @ApiOperation("测试list")
    public String TestAuth1() {
        return "TestAuth1 Success";
    }


    @GetMapping("/test/update")
    @PreAuthorize("hasAuthority('admin:seat:update')")
    @ApiOperation("测试update")
    public String TestAuth2() {
        return "TestAuth2 Success";
    }


    @GetMapping("/test/delete")
    @PreAuthorize("hasAuthority('admin:seat:delete')")
    @ApiOperation("测试delete")
    public String TestAuth3() {
        return "TestAuth3 Success";
    }

}
