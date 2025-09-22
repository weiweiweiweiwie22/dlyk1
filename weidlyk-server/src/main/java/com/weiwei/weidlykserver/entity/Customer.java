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
 * 客户表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_customer")
@Schema(name = "Customer", description = "客户表")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，客户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "线索ID")
    @TableField("clue_id")
    private Integer clueId;

    @Schema(description = "选购产品")
    @TableField("product")
    private Integer product;

    @Schema(description = "客户描述")
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
