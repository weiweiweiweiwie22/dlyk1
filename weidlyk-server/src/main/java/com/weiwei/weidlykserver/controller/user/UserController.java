package com.weiwei.weidlykserver.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api")
@Tag(name = "后台管理系统登录管理1")
public class UserController {
    @Resource
    private UserService userService;
    @Operation(summary = "登录")
    @GetMapping("/login/info")
    public Result<User> loginInfo(Authentication  authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.ok(user);
    }

    @Operation(summary = "免登录")
    @GetMapping("/login/free")
    public Result<User> freeLogin() {
        return Result.ok();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/users")
    public Result<IPage<User>> getUserInfo(@RequestParam long current, @RequestParam long size) {
        IPage<User> page = new Page<>(current, size);
        IPage<User> list = userService.pageItem(page);
        return Result.ok(list);
    }


}
