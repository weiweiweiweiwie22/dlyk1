package com.weiwei.weidlykserver.controller.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.dto.activity.ActivityAddDto;
import com.weiwei.weidlykserver.dto.activity.ActivityRemarkAddDto;
import com.weiwei.weidlykserver.dto.activity.ActivityUpdateDto;
import com.weiwei.weidlykserver.entity.Activity;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.query.ActivityQuery;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.ActivityRemarkService;
import com.weiwei.weidlykserver.service.ActivityService;
import com.weiwei.weidlykserver.vo.ActivityDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 市场活动表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api/activity")
@Tag(name = "活动管理")
public class ActivityController {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @Resource
    private ActivityService activityService;
    @Operation(summary = "获取分页活动信息")
    @RequestMapping("/list")
    public Result<IPage<Activity>> getActivitiesInfo(@RequestParam("current") long current, @RequestParam("size") long size, ActivityQuery  query) {
        IPage<Activity> page = new Page<>(current, size);
        IPage<Activity> list = activityService.pageItem(page,query);
        return Result.ok(list);
    }

    @Operation(summary = "添加活动")
    @PostMapping("/add") // 新增操作应使用 @PostMapping
    // 1. 使用 @RequestBody 和新创建的 DTO
    // 2. 使用 @Validated 开启校验
    // 3. 注入 Authentication 获取当前登录用户
    public Result addActivity(@Validated @RequestBody ActivityAddDto activityAddDto, Authentication authentication) {
        // 从 Spring Security 上下文中获取当前登录用户
        User currentUser = (User) authentication.getPrincipal();
        Integer createById = currentUser.getId();

        // 调用 service 层方法
        activityService.addActivity(activityAddDto, createById);

        return Result.ok();
    }

    @Operation(summary = "获取活动详情")
    @GetMapping("/detail/{id}")
    public Result<ActivityDetailVo> getActivityDetail(@PathVariable Integer id) {
        ActivityDetailVo activityDetailVo = activityService.getActivityDetailById(id);
        return Result.ok(activityDetailVo);
    }

    @Operation(summary = "修改活动信息")
    @PostMapping("/update")
    public Result updateActivity(@Validated @RequestBody ActivityUpdateDto activityUpdateDto, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Integer editById  = currentUser.getId();
        activityService.updateActivityById(activityUpdateDto,editById);
        return Result.ok();
    }

    @Operation(summary = "添加活动的备注信息")
    @PostMapping("remark/add")
    public Result addActivityRemark(@Validated @RequestBody ActivityRemarkAddDto activityRemarkAddDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        activityRemarkService.addActivityRemark(activityRemarkAddDto, user.getId());
        return Result.ok();
    }

    @Operation(summary = "删除活动")
    @PostMapping("/delete/{id}")
    public Result deleteActivity(@PathVariable Integer id) {
        activityService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "批量删除活动")
    @PostMapping("/delete/batch")
    public Result deleteBatchActivities(@RequestBody Integer[] ids) {
        activityService.removeByIds(List.of(ids));
        return Result.ok();
    }

    @Operation(summary = "获取活动列表")
    @GetMapping("/all")
    public Result<List<Activity>> getAllActivities() {
        List<Activity> activityList = activityService.list();
        return Result.ok(activityList);
    }

}
