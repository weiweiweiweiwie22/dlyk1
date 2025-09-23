package com.weiwei.weidlykserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 添加JavaTimeModule来支持Java 8的日期和时间API
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}