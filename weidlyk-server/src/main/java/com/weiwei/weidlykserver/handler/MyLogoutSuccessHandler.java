package com.weiwei.weidlykserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.weiwei.weidlykserver.constant.RedisConstant.REDIS_JWT_KEY_PREFIX;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. 从 Spring Security 的上下文中获取用户信息
        // 如果 authentication 为 null，说明用户可能是通过无效的 token 访问的登出接口，
        // 此时无需处理 Redis，直接返回成功即可。
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();

            // 2. 构造该用户在 Redis 中存储 JWT 的 key
            String redisKey = REDIS_JWT_KEY_PREFIX + user.getId();

            // 3. 从 Redis 中删除该 key，实现 token 失效
            stringRedisTemplate.delete(redisKey);
        }

        // 4. 构造成功的响应结果
        Result<String> result = Result.ok("退出成功");
        String resultJSON = objectMapper.writeValueAsString(result);

        // 5. 将成功信息响应给前端
        ResponseUtils.write(response, resultJSON);
    }
}