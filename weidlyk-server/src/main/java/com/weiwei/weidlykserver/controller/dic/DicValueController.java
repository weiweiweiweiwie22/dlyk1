package com.weiwei.weidlykserver.controller.dic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.DicValue;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.DicValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典值表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api/dicValue")
@Tag(name = "字典值管理", description = "字典值相关接口")
public class DicValueController {
    @Resource
    private DicValueService dicValueService;

    @Operation(summary = "获取所有字典值")
    @GetMapping("/all")
    public Result<List<DicValue>> list() {
        return Result.ok(dicValueService.list());
    }

    @Operation(summary = "分页查询字典值")
    @GetMapping("/list")
    public Result<Page<DicValue>> listByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String typeCode,
            @RequestParam(required = false) String typeValue) {
        Page<DicValue> page = new Page<>(current, size);
        LambdaQueryWrapper<DicValue> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(typeCode)) {
            wrapper.eq(DicValue::getTypeCode, typeCode);
        }
        if (StringUtils.hasText(typeValue)) {
            wrapper.like(DicValue::getTypeValue, typeValue);
        }
        wrapper.orderByAsc(DicValue::getOrder);
        return Result.ok(dicValueService.page(page, wrapper));
    }

    @Operation(summary = "根据类型代码获取字典值列表")
    @GetMapping("/listByTypeCode/{typeCode}")
    public Result<List<DicValue>> listByTypeCode(@PathVariable String typeCode) {
        LambdaQueryWrapper<DicValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DicValue::getTypeCode, typeCode);
        wrapper.orderByAsc(DicValue::getOrder);
        return Result.ok(dicValueService.list(wrapper));
    }

    @Operation(summary = "新增字典值")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody DicValue dicValue) {
        return Result.ok(dicValueService.save(dicValue));
    }

    @Operation(summary = "修改字典值")
    @PutMapping("/edit")
    public Result<Boolean> edit(@RequestBody DicValue dicValue) {
        return Result.ok(dicValueService.updateById(dicValue));
    }

    @Operation(summary = "删除字典值")
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        return Result.ok(dicValueService.removeById(id));
    }

    @Operation(summary = "批量删除字典值")
    @DeleteMapping("/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return Result.ok(dicValueService.removeBatchByIds(ids));
    }

    // 保留原有的业务接口
    @Operation(summary = "获取称呼列表")
    @GetMapping("/appellation/all")
    public Result<List<DicValue>> getAllAppellationPage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "appellation");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }

    @Operation(summary = "获取线索状态列表")
    @GetMapping("/status/all")
    public Result<List<DicValue>> getAllStatusPage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "clueState");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }

    @Operation(summary = "获取意向产品列表")
    @GetMapping("/intentionProduct/all")
    public Result<List<DicValue>> getAllProductPage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "intentionProduct");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }

    @Operation(summary = "获取需要贷款列表")
    @GetMapping("/needsLoan/all")
    public Result<List<DicValue>> getAllNeedsLoanPage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "needLoan");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }

    @Operation(summary = "获取意向状态列表")
    @GetMapping("/intentionStatus/all")
    public Result<List<DicValue>> getAllIntentionStatePage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "intentionState");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }

    @Operation(summary = "获取线索来源列表")
    @GetMapping("/source/all")
    public Result<List<DicValue>> getAllSourcePage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "source");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }

    @Operation(summary = "获取跟踪方式列表")
    @GetMapping("/noteWay/all")
    public Result<List<DicValue>> getAllTrackWayPage() {
        LambdaQueryWrapper<DicValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DicValue::getTypeCode, "noteWay");
        List<DicValue> dicValueList = dicValueService.list(queryWrapper);
        return Result.ok(dicValueList);
    }
}
