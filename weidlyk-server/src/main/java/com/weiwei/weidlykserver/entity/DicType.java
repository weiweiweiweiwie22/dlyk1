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
 * 字典类型表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_dic_type")
@Schema(name = "DicType", description = "字典类型表")
public class DicType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键，自动增长，字典类型ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "字典类型代码")
    @TableField("type_code")
    private String typeCode;

    @Schema(description = "字典类型名称")
    @TableField("type_name")
    private String typeName;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;
}
