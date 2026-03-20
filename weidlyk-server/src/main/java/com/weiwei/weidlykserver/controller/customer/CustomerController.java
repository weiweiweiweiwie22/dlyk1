package com.weiwei.weidlykserver.controller.customer;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.CustomerService;
import com.weiwei.weidlykserver.vo.CustomerDetailVo;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.security.core.Authentication;
import com.weiwei.weidlykserver.entity.User;

@RestController
@RequestMapping("/api") // 类上已经定义了前缀 /api
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @Operation(summary = "分页筛选获取客户列表")
    @GetMapping("/customers") // 最终路径：/api/customers
    public Result<IPage<CustomerPageVo>> listCustomersByPage(Page<CustomerPageVo> page, CustomerQuery query) {
        IPage<CustomerPageVo> pageResult = customerService.getCustomerPage(page, query);
        return Result.ok(pageResult);
    }

    @Operation(summary = "导出客户Excel")
    @GetMapping("/customer/export") // 最终路径：/api/customer/export
    public void exportCustomers(CustomerQuery query, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("客户列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<CustomerExportVo> list = customerService.listCustomersForExport(query);

        EasyExcel.write(response.getOutputStream(), CustomerExportVo.class)
                .sheet("客户列表")
                .doWrite(list);
    }

    @Operation(summary = "导出选中的客户Excel")
    @PostMapping("/customer/export/selected") // 最终路径：/api/customer/export/selected
    public void exportSelectedCustomers(@RequestBody List<Integer> ids, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("选中的客户列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<CustomerExportVo> list = customerService.listCustomersForExportByIds(ids);

        EasyExcel.write(response.getOutputStream(), CustomerExportVo.class)
                .sheet("选中的客户")
                .doWrite(list);
    }

    @Operation(summary = "获取客户详情")
    @GetMapping("/customer/detail/{id}") // 【关键修改】：去掉了多余的 /api，最终路径：/api/customer/detail/{id}
    public Result detail(@PathVariable Integer id) {
        CustomerDetailVo vo = customerService.getCustomerDetailById(id);
        return Result.ok(vo);
    }

    // 在 CustomerController.java 中添加

    @Operation(summary = "根据ID获取客户原始信息(用于编辑)")
    @GetMapping("/customer/{id}")
    public Result<Customer> getCustomer(@PathVariable("id") Integer id) {
        Customer customer = customerService.getById(id);
        return Result.ok(customer);
    }

    @Operation(summary = "修改客户信息")
    @PostMapping("/customer/update")
    public Result update(@RequestBody Customer customer, Authentication authentication) {
        // 1. 从安全上下文中获取当前登录的操作人
        User currentUser = (User) authentication.getPrincipal();
        Integer operatorId = currentUser.getId();

        // 2. 这里的 customer 对象里已经包含了前端传来的 id (客户ID)
        // 同时也包含修改后的 description, product 等字段
        boolean success = customerService.updateCustomer(customer, operatorId);

        return success ? Result.ok() : Result.fail(500, "修改客户失败");
    }
}