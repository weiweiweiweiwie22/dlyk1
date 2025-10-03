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
 * 交易表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_tran")
@Schema(name = "Tran", description = "交易表")
public class Tran implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，交易ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "交易流水号")
    @TableField("tran_no")
    private String tranNo;

    @Schema(description = "客户ID")
    @TableField("customer_id")
    private Integer customerId;

    @Schema(description = "交易金额")
    @TableField("money")
    private BigDecimal money;

    @Schema(description = "预计成交日期")
    @TableField("expected_LocalDateTime")
    private LocalDateTime expectedLocalDateTime;

    @Schema(description = "交易所处阶段")
    @TableField("stage")
    private Integer stage;

    @Schema(description = "交易描述")
    @TableField("description")
    private String description;

    @Schema(description = "下次联系时间")
    @TableField("next_contact_time")
    private LocalDateTime nextContactTime;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @TableField("create_by")
    private Integer createBy;

    @Schema(description = "编辑时间")
    @TableField("edit_time")
    private LocalDateTime editTime;

    @Schema(description = "编辑人")
    @TableField("edit_by")
    private Integer editBy;
}
