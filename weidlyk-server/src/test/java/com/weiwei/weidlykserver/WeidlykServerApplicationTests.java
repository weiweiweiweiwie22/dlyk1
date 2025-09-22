package com.weiwei.weidlykserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class WeidlykServerApplicationTests {

    @Test
    void contextLoads() {
    }

    // 添加这个新的测试方法
    @Test
    void generatePassword() {
        // 创建一个密码编码器
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密明文密码 "aaa111"
        String encodedPassword = passwordEncoder.encode("aaa111");
        // 打印加密后的结果
        System.out.println(encodedPassword);
    }
}