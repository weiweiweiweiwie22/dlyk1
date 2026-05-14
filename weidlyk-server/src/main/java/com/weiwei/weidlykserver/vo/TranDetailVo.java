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

    // 阶段信息
    private Integer stage;        // 阶段的字典ID（数字）
    private String stageName;     // 【新增】阶段的中文名称（如：跟进中、已成交）

    private String description;
    private LocalDateTime nextContactTime;

    // 关联信息
    private String customerName;  // 客户姓名
    private String activityName;  // 市场活动名称

    // 审计信息
    private LocalDateTime createTime;
    private String createByName;  // 【修改】改为 createByName，与 SQL 的 create_by_name 对应
    private LocalDateTime editTime;
    private String editByName;    // 【修改】改为 editByName，与 SQL 的 edit_by_name 对应
}