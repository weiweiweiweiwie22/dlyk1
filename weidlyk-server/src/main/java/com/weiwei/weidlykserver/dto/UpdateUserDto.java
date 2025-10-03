package com.weiwei.weidlykserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// UpdateUserDto.java
@Data
public class UpdateUserDto {

    @Schema(description = "主键，用户ID")
    private Integer id; // id 是必须的，用来知道要更新哪条记录

    @Schema(description = "登录账号")
    private String loginAct;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户手机")
    private String phone;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "账户是否没有过期，1正常 0已过期")
    private Integer accountNoExpired;

    @Schema(description = "密码是否没有过期，1正常 0已过期")
    private Integer credentialsNoExpired;

    @Schema(description = "账号是否没有锁定，1正常 0已锁定")
    private Integer accountNoLocked;

    @Schema(description = "账号是否启用，1启用 0禁用")
    private Integer accountEnabled;
}