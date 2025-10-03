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
 * 产品表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_product")
@Schema(name = "Product", description = "产品表")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，线索ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "产品名称")
    @TableField("name")
    private String name;

    @Schema(description = "官方指导起始价")
    @TableField("guide_price_s")
    private BigDecimal guidePriceS;

    @Schema(description = "官方指导最高价")
    @TableField("guide_price_e")
    private BigDecimal guidePriceE;

    @Schema(description = "经销商报价")
    @TableField("quotation")
    private BigDecimal quotation;

    @Schema(description = "状态 0在售 1售罄")
    @TableField("state")
    private Integer state;

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
