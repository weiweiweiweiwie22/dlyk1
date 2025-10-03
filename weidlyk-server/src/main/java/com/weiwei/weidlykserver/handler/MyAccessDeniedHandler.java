package com.weiwei.weidlykserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper; // 1. 引入 ObjectMapper
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.result.ResultCodeEnum; // 2. 引入你自己的返回码枚举
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.annotation.Resource; // 3. 引入 Resource
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 没有权限时的处理器
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    private ObjectMapper objectMapper; // 4. 注入 ObjectMapper 用于转换JSON

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 创建一个表示“访问被拒绝”的统一结果
        Result<Object> result = Result.fail(ResultCodeEnum.ACCESS_DENIED.getCode(), ResultCodeEnum.ACCESS_DENIED.getMessage());

        // 使用 ObjectMapper 将 Result 对象转换为 JSON 字符串
        String resultJSON = objectMapper.writeValueAsString(result);

        // 使用你自己的 ResponseUtils 将 JSON 返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}