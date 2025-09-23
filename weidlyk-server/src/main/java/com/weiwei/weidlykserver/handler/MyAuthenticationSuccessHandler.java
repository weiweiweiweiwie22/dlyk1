package com.weiwei.weidlykserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.util.JSONUtils;
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

import static com.weiwei.weidlykserver.constant.RedisConstant.*; // 导入常量

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
        String jwt = JWTUtils.createJWT(userJSON);

        // 根据 "rememberMe" 参数决定过期时间
        String rememberMe = request.getParameter("rememberMe");
        long expirationTime = Boolean.parseBoolean(rememberMe) ? REDIS_JWT_TTL_SEC : DEFAULT_REDIS_JWT_TTL_SEC;

        // **优化点：使用 set 方法一次性存入 JWT 并设置过期时间**
        stringRedisTemplate.opsForValue().set(REDIS_JWT_KEY_PREFIX + user.getId(), jwt, expirationTime, TimeUnit.SECONDS);

        // 封装并返回结果
        Result<String> result = Result.ok(jwt);
        String resultJSON = objectMapper.writeValueAsString(result);
        ResponseUtils.write(response, resultJSON);
    }
}