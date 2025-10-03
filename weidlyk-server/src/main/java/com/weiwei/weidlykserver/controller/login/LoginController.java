package com.weiwei.weidlykserver.controller.login;

import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "登录管理")

public class LoginController {
    @GetMapping("/login/info")
    @Operation(summary = "获取登录用户的信息")
    public Result<User> loginInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.ok(user);
    }

    @Operation(summary = "免登录")
    @GetMapping("/login/free")
    public Result<User> freeLogin() {
        return Result.ok();
    }



}
