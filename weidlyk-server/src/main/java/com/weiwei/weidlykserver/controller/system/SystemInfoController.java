package com.weiwei.weidlykserver.controller.system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.weiwei.weidlykserver.entity.SystemInfo;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.SystemInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource; // 注意：如果你用的是SpringBoot 2.x，这里改成 javax.annotation.Resource

/**

/**
 * <p>
 * 系统信息表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api/systemInfo")
public class SystemInfoController {
    @Resource
    private SystemInfoService systemInfoService;

    /**
     * 获取系统基础配置信息
     */
    @GetMapping("/info")
    public Result<SystemInfo> getSystemInfo() {
        // 通常系统配置表只有一条记录，这里默认取主键 id 为 1 的数据
        SystemInfo info = systemInfoService.getById(1);
        return Result.ok(info);
    }
}
