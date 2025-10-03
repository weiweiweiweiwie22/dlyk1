package com.weiwei.weidlykserver.dto.activity; // 建议放在 dto 包下

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(name = "ActivityAddDto", description = "新增市场活动DTO")
public class ActivityAddDto {

    @Schema(description = "活动名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "活动名称不能为空")
    private String name;

    @Schema(description = "负责人ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "必须指定负责人")
    private Integer ownerId;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "活动预算", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "活动预算不能为空")
    @Positive(message = "活动预算必须为正数")
    private BigDecimal cost;

    @Schema(description = "活动描述")
    private String description;
}