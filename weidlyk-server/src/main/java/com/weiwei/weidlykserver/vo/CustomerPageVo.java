// 在 vo 包下，修改 CustomerPageVo.java
package com.weiwei.weidlykserver.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data // 建议加上 @Data 注解简化代码
public class CustomerPageVo {
    @Schema(description = "客户ID")
    private Integer id;

    @Schema(description = "姓名")
    private String fullName;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "负责人")
    private String ownerName;

    @Schema(description = "意向产品")
    private String productName; // <-- 新增字段

    @Schema(description = "下次联系时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 增加格式化注解
    private LocalDateTime nextContactTime;

    @Schema(description = "成为客户时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 增加格式化注解
    private LocalDateTime createTime;
}