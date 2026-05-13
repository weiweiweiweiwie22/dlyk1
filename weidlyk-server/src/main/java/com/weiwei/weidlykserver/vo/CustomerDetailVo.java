package com.weiwei.weidlykserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(name = "CustomerDetailVo", description = "客户详情视图对象")
public class CustomerDetailVo {
    private Integer id;
    private String fullName; // 客户姓名 (来自 t_clue)
    private String phone;    // 手机号 (来自 t_clue)
    private String ownerName;// 负责人名称 (来自 t_user)
    private String productName; // 意向产品名称 (来自 t_product 或字典)
    private String description;
    private LocalDateTime nextContactTime;
    private LocalDateTime createTime;
    private String createBy;
    private LocalDateTime editTime;
    private String editBy;
}