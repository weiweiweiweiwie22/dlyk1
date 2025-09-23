package com.weiwei.weidlykserver.filter;

import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import com.weiwei.weidlykserver.util.JWTUtils;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("/api/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }


        filterChain.doFilter(request, response);
    }
}