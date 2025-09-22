package com.weiwei.weidlykserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 配置API文档的基本信息
        return new OpenAPI().info(new Info()
                .title("微宏客管理系统系统 API文档")
                .version("1.0")
                .description("本文档详细描述了微宏客管理系统系统的后端接口。")
                .contact(new Contact().name("weiwei"))); // 作者信息
    }

    @Bean
    public GroupedOpenApi userApi() {
        // 将用户、角色、权限相关的接口分为一组
        return GroupedOpenApi.builder()
                .group("用户与权限管理") // 分组名称
                .pathsToMatch(
                        "/user/**",
                        "/role/**",
                        "/permission/**",
                        "/userRole/**",
                        "/rolePermission/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi marketingApi() {
        // 将市场活动、线索相关的接口分为一组
        return GroupedOpenApi.builder()
                .group("市场与线索管理")
                .pathsToMatch(
                        "/activity/**",
                        "/activityRemark/**",
                        "/clue/**",
                        "/clueRemark/**"
                )
                .build();
    }
    
    @Bean
    public GroupedOpenApi businessApi() {
        // 将客户、交易、产品相关的接口分为一组
        return GroupedOpenApi.builder()
                .group("客户与交易管理")
                .pathsToMatch(
                        "/customer/**",
                        "/customerRemark/**",
                        "/tran/**",
                        "/tranHistory/**",
                        "/tranRemark/**",
                        "/product/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi systemApi() {
        // 将字典、系统信息等相关的接口分为一组
        return GroupedOpenApi.builder()
                .group("系统与字典管理")
                .pathsToMatch(
                        "/dicType/**",
                        "/dicValue/**",
                        "/systemInfo/**"
                )
                .build();
    }
}