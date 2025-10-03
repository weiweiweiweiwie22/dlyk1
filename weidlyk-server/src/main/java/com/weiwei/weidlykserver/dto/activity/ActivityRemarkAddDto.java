package com.weiwei.weidlykserver.dto.activity; // 确保包名正确

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ActivityRemarkAddDto", description = "新增活动备注DTO")
public class ActivityRemarkAddDto {

    @Schema(description = "关联的活动ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "活动ID不能为空")
    private Integer activityId;

    @Schema(description = "备注内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "备注内容不能为空")
    private String noteContent;
}