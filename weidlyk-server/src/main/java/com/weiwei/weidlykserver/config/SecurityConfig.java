package com.weiwei.weidlykserver.config;

import com.weiwei.weidlykserver.handler.MyAuthenticationFailureHandler;
import com.weiwei.weidlykserver.handler.MyAuthenticationSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 注入自定义的认证成功处理器
    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    // 注入自定义的认证失败处理器
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder  passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 配置请求授权规则
                .authorizeHttpRequests(authorize -> authorize
                        // 2. 对 Knife4j/Swagger 和登录请求 的相关路径放行，允许匿名访问
                        .requestMatchers(
                                "/doc.html",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/api/login" // 放行登录处理URL
                        ).permitAll()
                        // 3. 除了上面放行的路径，其他所有请求都需要认证（登录）后才能访问
                        .anyRequest().authenticated()
                )
                // 4. 配置表单登录
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/api/login") // 指定处理登录请求的URL
                        .usernameParameter("loginAct") // 自定义接收用户名的参数名
                        .passwordParameter("loginPwd") // 自定义接收密码的参数名
                        .successHandler(myAuthenticationSuccessHandler) // 配置登录成功处理器
                        .failureHandler(myAuthenticationFailureHandler) // 配置登录失败处理器
                )
                // 5. 禁用 CSRF（跨站请求伪造）保护
                //    在前后端分离项目中，通常使用Token机制认证，CSRF的必要性不大。
                .csrf(AbstractHttpConfigurer::disable);

        // 6. 构建并返回 SecurityFilterChain 实例
        return http.build();
    }
}