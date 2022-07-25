package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.aop.LogAnnotation;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.service.BookorderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhj
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/order")
@Api("预定订单接口")
public class BookorderController {

    private final BookorderService bookorderService;

    public BookorderController(BookorderService bookorderService) {
        this.bookorderService = bookorderService;
    }

    @GetMapping("/getAll")
    @ApiOperation("获取全部订单")
    @LogAnnotation
    public Result getAllOrder() {
        return bookorderService.getAllOrder();
    }

    @PostMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancelOrder(String bookOrderId) {
        return bookorderService.cancelOrder(bookOrderId);
    }

}
