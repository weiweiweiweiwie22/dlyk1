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
 * 交易跟踪记录表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_tran_remark")
@Schema(name = "TranRemark", description = "交易跟踪记录表")
public class TranRemark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，交易备注ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "交易ID")
    @TableField("tran_id")
    private Integer tranId;

    @Schema(description = "跟踪方式")
    @TableField("note_way")
    private Integer noteWay;

    @Schema(description = "跟踪内容")
    @TableField("note_content")
    private String noteContent;

    @Schema(description = "跟踪时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "跟踪人")
    @TableField("create_by")
    private Integer createBy;

    @Schema(description = "编辑时间")
    @TableField("edit_time")
    private LocalDateTime editTime;

    @Schema(description = "编辑人")
    @TableField("edit_by")
    private Integer editBy;

    @Schema(description = "删除状态（0正常，1删除）")
    @TableField("deleted")
    private Integer deleted;
}
