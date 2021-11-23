package org.lxzx.boot.enums;

import lombok.Data;
import lombok.Getter;

public enum ResultCode {
    SUCCESS(true, 200, "请求成功"),
    UNKNOWN_ERROR(false, 400, "未知错误"),
    NOTFIND_ERROR(false, 404, "访问错误"),
    REQUEST_ERROR(false, 405, "访问异常"),
    SERVER_ERROR(false, 500, "服务器错误"),

    BAD_CREDENTIALS(false, 3001, "用户名或密码输入错误！"),
    PARAMS_NOT_FULL(false, 3002, "参数不全！"),
    AUTHORIZATION_PASS(false, 3003, "登录过期，请重新登录"),
    CREDENTIALS_EXPIRED(false, 3004, "其他错误类型");


    private Boolean success;
    private Integer code;
    private String message;

    ResultCode(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
