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
 * 交易历史记录表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_tran_history")
@Schema(name = "TranHistory", description = "交易历史记录表")
public class TranHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，交易记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "交易ID")
    @TableField("tran_id")
    private Integer tranId;

    @Schema(description = "交易阶段")
    @TableField("stage")
    private Integer stage;

    @Schema(description = "交易金额")
    @TableField("money")
    private BigDecimal money;

    @Schema(description = "交易预计成交时间")
    @TableField("expected_LocalDateTime")
    private LocalDateTime expectedLocalDateTime;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @TableField("create_by")
    private Integer createBy;
}
