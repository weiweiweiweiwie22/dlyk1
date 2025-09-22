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
 * 系统信息表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_system_info")
@Schema(name = "SystemInfo", description = "系统信息表")
public class SystemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("system_code")
    private String systemCode;

    @TableField("name")
    private String name;

    @TableField("site")
    private String site;

    @TableField("logo")
    private String logo;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("keywords")
    private String keywords;

    @TableField("shortcuticon")
    private String shortcuticon;

    @TableField("tel")
    private String tel;

    @TableField("weixin")
    private String weixin;

    @TableField("email")
    private String email;

    @TableField("address")
    private String address;

    @TableField("version")
    private String version;

    @TableField("closeMsg")
    private String closeMsg;

    @TableField("isopen")
    private String isopen;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("create_by")
    private Integer createBy;

    @TableField("edit_time")
    private LocalDateTime editTime;

    @TableField("edit_by")
    private Integer editBy;
}
