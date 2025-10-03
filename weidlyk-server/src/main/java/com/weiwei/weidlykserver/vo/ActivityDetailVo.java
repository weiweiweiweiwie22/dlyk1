package com.weiwei.weidlykserver.vo;

import com.weiwei.weidlykserver.entity.ActivityRemark;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "ActivityDetailVo", description = "市场活动详情VO")
public class ActivityDetailVo {

    @Schema(description = "主键，活动ID")
    private Integer id;

    @Schema(description = "活动负责人ID")
    private Integer ownerId;

    @Schema(description = "活动负责人姓名")
    private String ownerName;

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

    @Schema(description = "活动创建时间")
    private LocalDateTime createTime;

    @Schema(description = "活动创建人ID")
    private Integer createBy;

    @Schema(description = "活动创建人姓名")
    private String createByName;

    @Schema(description = "活动编辑时间")
    private LocalDateTime editTime;

    @Schema(description = "活动编辑人ID")
    private Integer editBy;

    @Schema(description = "活动编辑人姓名")
    private String editByName;

    @Schema(description = "活动备注列表")
    private List<ActivityRemarkVo> remarkList;
}