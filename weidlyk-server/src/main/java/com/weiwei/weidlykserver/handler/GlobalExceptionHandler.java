//package com.weiwei.weidlykserver.handler;
//
//import com.bjpowernode.result.CodeEnum;
//import com.bjpowernode.result.R;
//import com.weiwei.weidlykserver.result.Result;
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
///**
// * 统一异常处理类，controller发生了异常，统一用该类进行处理
// *
// */
//@RestControllerAdvice //aop。拦截标注了@RestController的controller中的所有方法
////@ControllerAdvice //aop。拦截标注了@Controller的controller中的所有方法
//public class GlobalExceptionHandler {
//
//    /**
//     * 异常处理的方法（controller方法发生了异常，那么就使用该方法来处理）
//     *
//     * @return
//     */
//    @ExceptionHandler(value = Exception.class)
//    public Result handException(Exception e) {
//        e.printStackTrace(); //在控制台打印异常信息
//        return Result.FAIL(e.getMessage());
//    }
//
//    /**
//     * 异常的精确匹配，先精确匹配，匹配不到了，就找父类的异常处理
//     *
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = DataAccessException.class)
//    public Result handException3(DataAccessException e) {
//        e.printStackTrace(); //在控制台打印异常信息
//        return Result.FAIL(CodeEnum.DATA_ACCESS_EXCEPTION);
//    }
//
//    /**
//     * 权限不足的异常处理
//     *
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public Result handException(AccessDeniedException e) {
//        e.printStackTrace(); //在控制台打印异常信息
//        return Result.FAIL(CodeEnum.ACCESS_DENIED);
//    }
//}
