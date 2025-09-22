package com.weiwei.weidlykserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.weiwei.weidlykserver.mapper")
public class WeidlykServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeidlykServerApplication.class, args);
    }

}
