package com.weiwei.weidlykserver.controller.permission;

import com.weiwei.weidlykserver.entity.Permission;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api")
public class PermissionController {
    @Resource
    private PermissionService permissionService; // 确保你已创建 PermissionService

    @Operation(summary = "获取当前登录用户的菜单")
    @GetMapping("/permission/menus")
    public Result<List<Permission>> getCurrentUserMenus(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Permission> menuList = permissionService.getMenusByUserId(currentUser.getId());
        return Result.ok(menuList);
    }

}
