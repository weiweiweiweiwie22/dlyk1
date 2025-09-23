package com.weiwei.weidlykserver.handler;

import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 统一异常处理类，controller发生了异常，统一用该类进行处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     * @param e 业务异常
     * @return 统一的Result对象
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        e.printStackTrace(); // 在控制台打印异常信息，便于调试
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理所有其他未捕获的异常
     *
     * @return 统一的Result对象
     */
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace(); //在控制台打印异常信息
        return Result.fail(500, "服务器内部错误");
    }
}