package com.weiwei.weidlykserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 配置请求授权规则
            .authorizeHttpRequests(authorize -> authorize
                // 2. 对 Knife4j/Swagger 的相关路径放行，允许匿名访问
                .requestMatchers(
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**" 
                ).permitAll()
                // 3. 除了上面放行的路径，其他所有请求都需要认证（登录）后才能访问
                .anyRequest().authenticated()
            )
            // 4. 使用 Spring Security 默认的表单登录页面
            .formLogin(formLogin -> formLogin
                    .loginPage("/login") // 指定默认的登录页面URL
                    .permitAll() // 允许所有用户访问登录页面
            )
            // 5. 暂时禁用 CSRF（跨站请求伪造）保护，便于开发调试
            //    前后端分离项目通常使用 Token 认证，CSRF 不是主要问题
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}