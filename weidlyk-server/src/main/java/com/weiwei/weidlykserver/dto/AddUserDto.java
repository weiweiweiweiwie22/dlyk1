package com.weiwei.weidlykserver.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserDto extends BaseDto{

    @Schema(description = "主键，自动增长，用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "登录账号")
    @TableField("login_act")
    private String loginAct;

    @Schema(description = "登录密码")
    @TableField("login_pwd")
    private String loginPwd;

    @Schema(description = "用户姓名")
    @TableField("name")
    private String name;

    @Schema(description = "用户手机")
    @TableField("phone")
    private String phone;

    @Schema(description = "用户邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "账户是否没有过期，0已过期 1正常")
    @TableField("account_no_expired")
    private Integer accountNoExpired;

    @Schema(description = "密码是否没有过期，0已过期 1正常")
    @TableField("credentials_no_expired")
    private Integer credentialsNoExpired;

    @Schema(description = "账号是否没有锁定，0已锁定 1正常")
    @TableField("account_no_locked")
    private Integer accountNoLocked;

    @Schema(description = "账号是否启用，0禁用 1启用")
    @TableField("account_enabled")
    private Integer accountEnabled;

}
