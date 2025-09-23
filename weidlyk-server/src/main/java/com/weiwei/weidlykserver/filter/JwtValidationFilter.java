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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.weiwei.weidlykserver.constant.RedisConstant.REDIS_JWT_KEY_PREFIX;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

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
            String redisJwt = stringRedisTemplate.opsForValue().get(REDIS_JWT_KEY_PREFIX + userId);

            if (!StringUtils.hasText(redisJwt) || !redisJwt.equals(jwt)) {
                throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
            }

            // 核心改动：使用注入的objectMapper来反序列化
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