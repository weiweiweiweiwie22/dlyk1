package com.weiwei.weidlykserver.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import com.weiwei.weidlykserver.util.JWTUtils;
import com.weiwei.weidlykserver.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.weiwei.weidlykserver.constant.RedisConstant.DEFAULT_REDIS_JWT_TTL_SEC;
import static com.weiwei.weidlykserver.constant.RedisConstant.REDIS_JWT_KEY_PREFIX;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    // ⬇️⬇️⬇️ 就是这一行代码，您之前缺失了 ⬇️⬇️⬇️
    // 在类的内部声明并初始化 log 变量
    private static final Logger log = LoggerFactory.getLogger(JwtValidationFilter.class);
    // ⬆️⬆️⬆️ 就是这一行代码，您之前缺失了 ⬆️⬆️⬆️

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 如果是登录请求，直接放行
        if ("/api/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = request.getHeader("Authorization");

            if (!StringUtils.hasText(jwt)) {
                throw new BusinessException(ResultCodeEnum.TOKEN_NOT_PROVIDED);
            }

            DecodedJWT decodedJWT = JWTUtils.verifyJWT(jwt);
            Integer userId = decodedJWT.getClaim("userId").asInt();
            String redisKey = REDIS_JWT_KEY_PREFIX + userId;

            String redisJwt = stringRedisTemplate.opsForValue().get(redisKey);

            if (!StringUtils.hasText(redisJwt) || !redisJwt.equals(jwt)) {
                throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
            }

            boolean rememberMe = decodedJWT.getClaim("rememberMe").asBoolean();

            // 关键的诊断日志！
            log.info("Checking token for user: {}. RememberMe flag is: {}", userId, rememberMe);

            if (!rememberMe) {
                log.info("Refreshing token expiration for user: {}", userId);
                stringRedisTemplate.expire(redisKey, DEFAULT_REDIS_JWT_TTL_SEC, TimeUnit.SECONDS);
            }

            String userJSON = decodedJWT.getClaim("user").asString();
            User user = objectMapper.readValue(userJSON, User.class);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (BusinessException e) {
            Result<Object> result = Result.fail(e.getCode(), e.getMessage());
            String resultJSON = objectMapper.writeValueAsString(result);
            ResponseUtils.write(response, resultJSON);
        }
    }
}