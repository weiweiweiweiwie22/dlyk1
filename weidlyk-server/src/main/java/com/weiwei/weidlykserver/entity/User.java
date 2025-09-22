package com.weiwei.weidlykserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Getter
@Setter
@TableName("t_user")
@Schema(name = "User", description = "用户表")
public class User implements UserDetails,Serializable {

    private static final long serialVersionUID = 1L;

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

    @Schema(description = "最近登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    //自定义字段

    //角色列表
    private List<String> roleList;

    //权限列表
    private List<String> permissionList;

    // UserDetails的方法
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        this.getRoleList().forEach(role -> list.add(new SimpleGrantedAuthority(role)));
        this.getPermissionList().forEach(permission -> list.add(new SimpleGrantedAuthority(permission)));
        return List.of();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.getLoginPwd();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.getLoginAct();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return this.getAccountNoExpired() == 1;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.getAccountNoLocked() == 1;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return this.getCredentialsNoExpired() == 1;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.getAccountEnabled() == 1;
    }
}
