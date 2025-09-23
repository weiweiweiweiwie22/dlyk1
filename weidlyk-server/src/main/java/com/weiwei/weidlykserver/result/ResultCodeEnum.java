package com.weiwei.weidlykserver.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(500, "失败"),

    //== Token 相关错误码 ==//
    TOKEN_NOT_PROVIDED(901, "未提供Token"),
    TOKEN_EXPIRED(902, "Token已过期"),
    TOKEN_INVALID(903, "无效的Token");


    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}