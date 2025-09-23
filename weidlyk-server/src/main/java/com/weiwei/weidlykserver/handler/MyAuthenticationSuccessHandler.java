package com.weiwei.weidlykserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper; // 1. 导入 ObjectMapper
import com.weiwei.weidlykserver.entity.User;

import com.weiwei.weidlykserver.util.JSONUtils;
import com.weiwei.weidlykserver.util.JWTUtils;
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.annotation.Resource; // 2. 确保导入 jakarta.annotation.Resource
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Resource // 3. 注入 Spring 管理的 ObjectMapper
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String userJSON = JSONUtils.toJSON(user);
        // 生成 JWT，用户信息作为负载
        String jwt = JWTUtils.createJWT(userJSON);

        redisService.setValue(Constants.REDIS_JWT_KEY+user.getId(), jwt);

        // 4. 使用注入的 objectMapper 来转换 JSON，而不是 JSONUtils
        String resultJSON = objectMapper.writeValueAsString(result);

        ResponseUtils.write(response, resultJSON);
    }
}