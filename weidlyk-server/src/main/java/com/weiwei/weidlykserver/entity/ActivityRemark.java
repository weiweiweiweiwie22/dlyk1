package com.weiwei.weidlykserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 市场活动备注表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_activity_remark")
@Schema(name = "ActivityRemark", description = "市场活动备注表")
public class ActivityRemark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，活动备注ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "活动ID")
    @TableField("activity_id")
    private Integer activityId;

    @Schema(description = "备注内容")
    @TableField("note_content")
    private String noteContent;

    @Schema(description = "备注创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "备注创建人")
    @TableField("create_by")
    private Integer createBy;

    @Schema(description = "备注编辑时间")
    @TableField("edit_time")
    private LocalDateTime editTime;

    @Schema(description = "备注编辑人")
    @TableField("edit_by")
    private Integer editBy;

    @Schema(description = "删除状态（0正常，1删除）")
    @TableField("deleted")
    private Integer deleted;
}
