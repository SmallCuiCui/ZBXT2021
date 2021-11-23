package org.lxzx.boot.dto;

import lombok.Getter;
import org.lxzx.boot.enums.ResultCode;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Result {

    // 是否成功
    private Boolean success;

    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private Map<String, Object> data;

    private Result() {

    }

    private Result(ResultCode resultCode) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static Result ok() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result error() {
        return new Result(ResultCode.UNKNOWN_ERROR);
    }

    public static Result error(ResultCode resultCode) {
        return new Result(resultCode);
    }

    public Result code(Integer code) {
        this.code = code;
        return this;
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public Result data(Object obj) {
        if(this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put("data", obj);
        return this;
    }

    public Result data(String key, Object obj) {
        if(this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, obj);
        return this;
    }
}
