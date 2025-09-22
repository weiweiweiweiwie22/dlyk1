package com.weiwei.weidlykserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 市场活动表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_activity")
@Schema(name = "Activity", description = "市场活动表")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，活动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "活动所属人ID")
    @TableField("owner_id")
    private Integer ownerId;

    @Schema(description = "活动名称")
    @TableField("name")
    private String name;

    @Schema(description = "活动开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @Schema(description = "活动预算")
    @TableField("cost")
    private BigDecimal cost;

    @Schema(description = "活动描述")
    @TableField("description")
    private String description;

    @Schema(description = "活动创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "活动创建人")
    @TableField("create_by")
    private Integer createBy;

    @Schema(description = "活动编辑时间")
    @TableField("edit_time")
    private LocalDateTime editTime;

    @Schema(description = "活动编辑人")
    @TableField("edit_by")
    private Integer editBy;
}
