package com.example.graduationlhj.common.lang;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Result {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String message;
    /**
     * 查询到的结果数据，
     */
    private Object data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Result(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }
}