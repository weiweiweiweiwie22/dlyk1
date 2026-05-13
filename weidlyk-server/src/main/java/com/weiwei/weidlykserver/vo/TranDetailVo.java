package com.weiwei.weidlykserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(name = "TranDetailVo", description = "交易详情视图对象")
public class TranDetailVo {
    private Integer id;
    private String tranNo;
    private BigDecimal money;
    private LocalDateTime expectedDate;
    private Integer stage;
    private String description;
    private LocalDateTime nextContactTime;
    
    // 关联信息
    private String customerName; // 客户姓名
    private String activityName; // 市场活动名称
    
    // 审计信息
    private LocalDateTime createTime;
    private String createBy;     // 创建人姓名
    private LocalDateTime editTime;
    private String editBy;       // 编辑人姓名
}