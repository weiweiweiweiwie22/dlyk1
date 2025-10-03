package com.weiwei.weidlykserver.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 这是 Spring Boot 官方推荐的、定制化 Jackson ObjectMapper 的最佳方式。
     * 它通过一个 Customizer Bean 来对 Spring Boot 自动配置的 ObjectMapperBuilder 进行补充设置。
     * 这样做的好处是，我们既享受了 Spring Boot 的所有默认优良配置，又能安全地加入我们自己的配置，
     * 同时不会破坏像 Knife4j/SpringDoc 这类第三方库的正常工作。
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // 针对 LocalDateTime 类型的全局序列化配置
            builder.serializerByType(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));

            // 针对 LocalDateTime 类型的全局反序列化配置
            builder.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
        };
    }
}