package com.weiwei.weidlykserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper; // 导入ObjectMapper
import com.weiwei.weidlykserver.result.Result;
// 删掉 import com.weiwei.weidlykserver.util.JSONUtils;
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.annotation.Resource; // 导入Resource
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Resource // 注入正确配置的ObjectMapper
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("失败");

        Result<Object> result = Result.fail();

        // 核心改动：直接使用注入的objectMapper
        String resultJSON = objectMapper.writeValueAsString(result);

        ResponseUtils.write(response, resultJSON);
    }
}