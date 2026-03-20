package com.weiwei.weidlykserver.controller.dic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.DicType;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.DicTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api/dicType")
@Tag(name = "字典类型管理", description = "字典类型相关接口")
public class DicTypeController {
    @Resource
    private DicTypeService dicTypeService;

    @Operation(summary = "获取所有字典类型")
    @GetMapping("/all")
    public Result<List<DicType>> list() {
        return Result.ok(dicTypeService.list());
    }

    @Operation(summary = "分页查询字典类型")
    @GetMapping("/list")
    public Result<Page<DicType>> listByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String typeCode,
            @RequestParam(required = false) String typeName) {
        Page<DicType> page = new Page<>(current, size);
        LambdaQueryWrapper<DicType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(typeCode)) {
            wrapper.like(DicType::getTypeCode, typeCode);
        }
        if (StringUtils.hasText(typeName)) {
            wrapper.like(DicType::getTypeName, typeName);
        }
        wrapper.orderByAsc(DicType::getId);
        return Result.ok(dicTypeService.page(page, wrapper));
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody DicType dicType) {
        return Result.ok(dicTypeService.save(dicType));
    }

    @Operation(summary = "修改字典类型")
    @PutMapping("/edit")
    public Result<Boolean> edit(@RequestBody DicType dicType) {
        return Result.ok(dicTypeService.updateById(dicType));
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        return Result.ok(dicTypeService.removeById(id));
    }

    @Operation(summary = "批量删除字典类型")
    @DeleteMapping("/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return Result.ok(dicTypeService.removeBatchByIds(ids));
    }
}
