package com.example.graduationlhj.controller;


import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.params.param.BookOrderParam;
import com.example.graduationlhj.service.BookorderService;
import org.springframework.web.bind.annotation.*;

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
public class BookorderController {

    private final BookorderService bookorderService;

    public BookorderController(BookorderService bookorderService) {
        this.bookorderService = bookorderService;
    }

    @GetMapping("/getAll")
    public Result getAllOrder() {
        return bookorderService.getAllOrder();
    }

    @PostMapping("/cancel")
    public Result cancelOrder(String bookOrderId) {
        return bookorderService.cancelOrder(bookOrderId);
    }

}
