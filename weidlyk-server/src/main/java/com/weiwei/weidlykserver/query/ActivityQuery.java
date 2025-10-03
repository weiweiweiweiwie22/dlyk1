package com.weiwei.weidlykserver.query;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "市场活动查询实体")
@Data
public class ActivityQuery {

    @Schema(description = "创建人")
    private Integer ownerId;

    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "活动预算")
    private BigDecimal cost;

    @Schema(description = "活动创建时间")
    private LocalDateTime createTime;

    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;
}
