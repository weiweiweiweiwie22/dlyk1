package com.weiwei.weidlykserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 字典值表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_dic_value")
@Schema(name = "DicValue", description = "字典值表")
public class DicValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，字典值ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "字典类型代码")
    @TableField("type_code")
    private String typeCode;

    @Schema(description = "字典值")
    @TableField("type_value")
    private String typeValue;

    @Schema(description = "字典值排序")
    @TableField("order")
    private Integer order;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;
}
