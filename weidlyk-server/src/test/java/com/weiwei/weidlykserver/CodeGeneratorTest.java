package com.weiwei.weidlykserver;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
public class CodeGeneratorTest {

    @Test
    void generateCode() {
        // 数据库连接配置
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/dlyk?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2b8", "root", "123456")

                // 1. 全局配置
                .globalConfig(builder -> {
                    builder.author("weiwei") // 设置作者
                            // ==================== ↓↓↓ 核心修改点在这里 ↓↓↓ ====================
                            .enableSpringdoc() // 使用 .enableSpringdoc() 替代 .enableSwagger() 来生成 OpenAPI 3 注解
                            // ==================== ↑↑↑ 核心修改点在这里 ↑↑↑ ====================
                            .outputDir(System.getProperty("user.dir") + "/weidlyk-server/src/main/java")
                            .commentLocalDateTime("yyyy-MM-dd");
                })

                // 2. 包配置
                .packageConfig(builder -> {
                    builder.parent("com.weiwei.weidlykserver")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/weidlyk-server/src/main/resources/mapper"));
                })

                // 3. 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "t_activity", "t_activity_remark", "t_clue", "t_clue_remark",
                                    "t_customer", "t_customer_remark", "t_dic_type", "t_dic_value",
                                    "t_permission", "t_product", "t_role", "t_role_permission",
                                    "t_system_info", "t_tran", "t_tran_history", "t_tran_remark",
                                    "t_user", "t_user_role"
                            )
                            .addTablePrefix("t_")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            .enableRestStyle()
                            .formatFileName("%sController");
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}