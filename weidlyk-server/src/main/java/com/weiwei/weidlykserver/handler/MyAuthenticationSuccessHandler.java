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
    private ObjectMapper objectMapper; // Spring会注入我们在ObjectMapperConfig中配置好的实例

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        // 核心改动：不再调用JWTUtils去序列化，直接在这里完成
        String userJSON = objectMapper.writeValueAsString(user);
        // 将序列化后的JSON传入JWT工具类
        String jwt = JWTUtils.createJWT(user.getId(), userJSON);

        String rememberMe = request.getParameter("rememberMe");
        long expirationTime = Boolean.parseBoolean(rememberMe) ? REDIS_JWT_TTL_SEC : DEFAULT_REDIS_JWT_TTL_SEC;

        stringRedisTemplate.opsForValue().set(REDIS_JWT_KEY_PREFIX + user.getId(), jwt, expirationTime, TimeUnit.SECONDS);

        Result<String> result = Result.ok(jwt);
        // 直接使用注入的 objectMapper
        String resultJSON = objectMapper.writeValueAsString(result);
        ResponseUtils.write(response, resultJSON);
    }
}