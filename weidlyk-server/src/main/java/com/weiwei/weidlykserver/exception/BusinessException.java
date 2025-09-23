package com.weiwei.weidlykserver.exception;

import com.weiwei.weidlykserver.result.ResultCodeEnum;
import lombok.Getter;

@Getter // lombok 注解，自动生成 getter 方法，方便获取 code
public class BusinessException extends RuntimeException {

    private final Integer code; // 业务状态码

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}