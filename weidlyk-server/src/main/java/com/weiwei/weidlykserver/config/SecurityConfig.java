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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
    public PasswordEncoder passwordEncoder() {
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
                .csrf(AbstractHttpConfigurer::disable)

                // 6. 配置 CORS 跨域
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 7. 构建并返回 SecurityFilterChain 实例
        return http.build();
    }

    /**
     * 创建并配置 CORS 规则
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ⬇️⬇️⬇️ 只修改这一行 ⬇️⬇️⬇️
        // 将 "*" 修改为你的具体前端地址
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8082"));
        // ⬆️⬆️⬆️ 只修改这一行 ⬆️⬆️⬆️

        // 允许的请求方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 是否允许携带凭证（如 cookies）
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有 URL 应用这个 CORS 配置
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}