package com.weiwei.weidlykserver.controller.user;

import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "登录")
    @GetMapping("/login/info")
    public Result<User> loginInfo(Authentication  authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.ok(user);
    }


}
