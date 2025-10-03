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
    TOKEN_INVALID(903, "无效的Token"),
    DATA_NOT_EXIST(701,"数据不存在"),
    USER_DELETE_SELF_ERROR(601, "不能删除自己"),
    IMPORT_ERROR(801, "导入失败"),
    CONVERT_ERROR(802, "转换失败"),
    ACCESS_DENIED(904, "拒绝访问");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}