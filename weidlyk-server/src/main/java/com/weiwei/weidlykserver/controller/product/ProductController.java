package com.weiwei.weidlykserver.controller.product;

import com.weiwei.weidlykserver.entity.Product;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api")
public class ProductController {
    @Resource
    private ProductService productService;
    @Operation(summary = "获取产品列表")
    @RequestMapping("/product/all")
    public Result<List<Product>> list() {
        return Result.ok(productService.list());
    }

}
