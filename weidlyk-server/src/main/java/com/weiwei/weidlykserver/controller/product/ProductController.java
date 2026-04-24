package com.weiwei.weidlykserver.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Product;
import com.weiwei.weidlykserver.query.ProductQuery;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 产品表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @Operation(summary = "获取所有产品列表")
    @GetMapping("/all")
    public Result<List<Product>> list() {
        return Result.ok(productService.list());
    }

    @Operation(summary = "分页查询产品列表")
    @GetMapping("/list")
    public Result<Page<Product>> listByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            ProductQuery query) {
        Page<Product> page = new Page<>(current, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StringUtils.hasText(query.getName())) {
                wrapper.like(Product::getName, query.getName());
            }
            if (query.getState() != null) {
                wrapper.eq(Product::getState, query.getState());
            }
        }
        wrapper.orderByDesc(Product::getCreateTime);
        return Result.ok(productService.page(page, wrapper));
    }

    @Operation(summary = "新增产品")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Product product) {
        product.setCreateTime(LocalDateTime.now());
        return Result.ok(productService.save(product));
    }

    @Operation(summary = "修改产品")
    @PutMapping("/edit")
    public Result<Boolean> edit(@RequestBody Product product) {
        product.setEditTime(LocalDateTime.now());
        return Result.ok(productService.updateById(product));
    }

    @Operation(summary = "删除产品")
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        return Result.ok(productService.removeById(id));
    }

    @Operation(summary = "批量删除产品")
    @DeleteMapping("/batchDelete")
    public Result<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        return Result.ok(productService.removeBatchByIds(ids));
    }

}
