package com.weiwei.weidlykserver.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(name = "ActivityUpdateDto", description = "修改市场活动DTO")
public class ActivityUpdateDto {

        @Schema(description = "主键，活动ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "活动ID不能为空") // ID在更新时是必需的
        private Integer id;

        // 以下是用户可以修改的字段
        @Schema(description = "活动负责人ID")
        private Integer ownerId;

        @Schema(description = "活动名称")
        private String name;

        @Schema(description = "活动开始时间")
        private LocalDateTime startTime;

        @Schema(description = "活动结束时间")
        private LocalDateTime endTime;

        @Schema(description = "活动预算")
        private BigDecimal cost;

        @Schema(description = "活动描述")
        private String description;
}