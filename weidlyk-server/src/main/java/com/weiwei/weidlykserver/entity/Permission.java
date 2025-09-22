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
 * 权限表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_permission")
@Schema(name = "Permission", description = "权限表")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("url")
    private String url;

    @TableField("type")
    private String type;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("order_no")
    private Integer orderNo;

    @TableField("icon")
    private String icon;
}
