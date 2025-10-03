package com.weiwei.weidlykserver.controller.dic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.weidlykserver.entity.DicValue;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.DicValueService;
import com.weiwei.weidlykserver.vo.CluePageVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api")
public class DicValueController {
    @Resource
    private DicValueService dicValueService;

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
