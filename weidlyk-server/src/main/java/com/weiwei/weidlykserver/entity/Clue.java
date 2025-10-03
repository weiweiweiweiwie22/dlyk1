package com.weiwei.weidlykserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 线索表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_clue")
@Schema(name = "Clue", description = "线索表")
public class Clue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，线索ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "线索所属人ID")
    @TableField("owner_id")
    private Integer ownerId;

    @Schema(description = "活动ID")
    @TableField("activity_id")
    private Integer activityId;

    @Schema(description = "姓名")
    @TableField("full_name")
    private String fullName;

    @Schema(description = "称呼")
    @TableField("appellation")
    private Integer appellation;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "微信号")
    @TableField("weixin")
    private String weixin;

    @Schema(description = "QQ号")
    @TableField("qq")
    private String qq;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "年龄")
    @TableField("age")
    private Integer age;

    @Schema(description = "职业")
    @TableField("job")
    private String job;

    @Schema(description = "年收入")
    @TableField("year_income")
    private BigDecimal yearIncome;

    @Schema(description = "地址")
    @TableField("address")
    private String address;

    @Schema(description = "是否需要贷款（0不需要，1需要）")
    @TableField("need_loan")
    private Integer needLoan;

    @Schema(description = "意向状态")
    @TableField("intention_state")
    private Integer intentionState;

    @Schema(description = "意向产品")
    @TableField("intention_product")
    private Integer intentionProduct;

    @Schema(description = "线索状态")
    @TableField("state")
    private Integer state;

    @Schema(description = "线索来源")
    @TableField("source")
    private Integer source;

    @Schema(description = "线索描述")
    @TableField("description")
    private String description;

    @Schema(description = "下次联系时间")
    @TableField("next_contact_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
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
