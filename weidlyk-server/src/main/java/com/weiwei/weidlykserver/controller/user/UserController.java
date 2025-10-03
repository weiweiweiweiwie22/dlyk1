package com.weiwei.weidlykserver.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.dto.AddUserDto;
import com.weiwei.weidlykserver.dto.UpdateUserDto;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.UserService;
import com.weiwei.weidlykserver.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Tag(name = "用户管理")
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "获取分页用户信息")
    @GetMapping("/users")
    // 修正后的版本
    public Result<IPage<User>> getUserInfo(@RequestParam("current") long current, @RequestParam("size") long size) {
        IPage<User> page = new Page<>(current, size);
        IPage<User> list = userService.pageItem(page);
        return Result.ok(list);
    }

    @Operation(summary = "获取所有用户信息")
    @GetMapping("/user/all")
    public Result<List<User>> getAllUserInfo() {
        return Result.ok(userService.getlist());
    }

    @Operation(summary = "根据ID获取用户详情信息")
    @GetMapping("/getUserDetail/{id}")
    public Result<UserVo> getUserDetail(@PathVariable Integer id) {
        UserVo userVo = userService.getDetailById(id);
        return Result.ok(userVo);
    }

    @Operation(summary = "添加用户")
    @PostMapping("/user/add")
    public Result addUser(@RequestBody AddUserDto userDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer LoginUserId = user.getId();
        int count = userService.saveUser(userDto, LoginUserId);
        return count > 0 ? Result.ok() : Result.fail();
    }

    @Operation(summary = "修改用户")
    @PostMapping("/user/update")
    public Result updateUser(@RequestBody UpdateUserDto updateUserDto, Authentication authentication) {
        User editor = (User) authentication.getPrincipal();
        Integer editorId = editor.getId();
        return userService.updateUser(updateUserDto, editorId) ? Result.ok() : Result.fail();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/user/delete/{id}")
    public Result deleteUser(@PathVariable Integer id,Authentication  authentication) {
        User nowUser = (User) authentication.getPrincipal();
        Integer nowUserId = nowUser.getId();
        return userService.removeUserById(id,nowUserId) ? Result.ok() : Result.fail();
    }

    @Operation(summary = "批量删除用户")
    @PostMapping("/user/delete/batch")
    public Result batchDeleteUser(@RequestBody Integer[] ids,Authentication authentication) {
        User nowUser = (User) authentication.getPrincipal();
        Integer nowUserId = nowUser.getId();
        return userService.removeBatchUsersByIds(ids,nowUserId) ? Result.ok() : Result.fail();
    }


}
