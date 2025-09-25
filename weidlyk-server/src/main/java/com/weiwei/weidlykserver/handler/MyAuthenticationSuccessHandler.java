package com.weiwei.weidlykserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
// 删掉 import com.weiwei.weidlykserver.util.JSONUtils;
import com.weiwei.weidlykserver.util.JWTUtils;
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.weiwei.weidlykserver.constant.RedisConstant.*;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String userJSON = objectMapper.writeValueAsString(user);

        // 1. 获取前端传来的 rememberMe 参数
        String rememberMeParam = request.getParameter("rememberMe");
        boolean rememberMe = Boolean.parseBoolean(rememberMeParam);

        // 2. 【修改】调用新的 createJWT 方法，传入 rememberMe 状态
        String jwt = JWTUtils.createJWT(user.getId(), userJSON, rememberMe);

        // 3. 根据 rememberMe 状态决定在 Redis 中的初始过期时间
        long expirationTime = rememberMe ? REDIS_JWT_TTL_SEC : DEFAULT_REDIS_JWT_TTL_SEC;

        stringRedisTemplate.opsForValue().set(REDIS_JWT_KEY_PREFIX + user.getId(), jwt, expirationTime, TimeUnit.SECONDS);

        Result<String> result = Result.ok(jwt);
        String resultJSON = objectMapper.writeValueAsString(result);
        ResponseUtils.write(response, resultJSON);
    }
}