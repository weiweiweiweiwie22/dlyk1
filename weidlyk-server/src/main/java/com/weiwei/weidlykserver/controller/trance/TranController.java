package com.weiwei.weidlykserver.controller.trance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Tran;
import com.weiwei.weidlykserver.entity.User; // 【新增】
import com.weiwei.weidlykserver.mapper.TranMapper;
import com.weiwei.weidlykserver.query.TranQuery;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.TranService;
import com.weiwei.weidlykserver.vo.TranDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // 【新增】从 Security 上下文获取用户
import org.springframework.web.bind.annotation.*;

@Tag(name = "交易管理接口")
@RestController
@RequestMapping("/api/tran")
public class TranController {

    @Autowired
    private TranService tranService;

    @Autowired
    private TranMapper tranMapper;

    @Operation(summary = "分页查询交易列表")
    @GetMapping("/list")
    public Result list(@RequestParam(value = "current", defaultValue = "1") Integer current,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       TranQuery query) {
        Page<Tran> page = new Page<>(current, size);
        IPage<Tran> tranPage = tranMapper.selectTranPage(page, query);
        return Result.ok(tranPage);
    }

    @Operation(summary = "创建交易")
    @PostMapping("/save")
    public Result save(@RequestBody Tran tran, Authentication authentication) {
        // 【核心修改】：通过 Authentication 获取当前用户 ID
        User user = (User) authentication.getPrincipal();
        tran.setCreateBy(user.getId());

        boolean success = tranService.saveTran(tran);
        return success ? Result.ok() : Result.fail(500, "创建交易失败");
    }

    @Operation(summary = "推进交易阶段")
    @PutMapping("/stage")
    public Result updateStage(@RequestBody Tran tran, Authentication authentication) {
        // 【核心修改】：通过 Authentication 获取当前用户 ID，解决 Missing attribute 报错
        User user = (User) authentication.getPrincipal();
        tran.setEditBy(user.getId());

        boolean success = tranService.updateTranStage(tran);
        return success ? Result.ok() : Result.fail(500, "阶段推进失败");
    }

    @Operation(summary = "获取交易详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Integer id) {
        TranDetailVo vo = tranMapper.selectTranDetailById(id);
        return Result.ok(vo);
    }
}