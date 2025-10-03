// 修改 controller.customer 包下的 CustomerController.java
package com.weiwei.weidlykserver.controller.customer;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Customer; // 引入 Customer
import com.weiwei.weidlykserver.query.CustomerQuery; // 引入 CustomerQuery
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.CustomerService; // 引入 CustomerService
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource; // 引入 Resource
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Resource // 使用 @Resource 注入
    private CustomerService customerService;

    @Operation(summary = "分页筛选获取客户列表")
    @GetMapping("/customers") // 使用 @GetMapping
    public Result<IPage<CustomerPageVo>> listCustomersByPage(Page<CustomerPageVo> page, CustomerQuery query) {
        IPage<CustomerPageVo> pageResult = customerService.getCustomerPage(page, query);
        return Result.ok(pageResult);
    }

    @Operation(summary = "导出客户Excel")
    @GetMapping("/customer/export") // 1. 使用 GET 请求
    public void exportCustomers(CustomerQuery query, HttpServletResponse response) throws IOException {
        // 2. 设置HTTP响应头，告诉浏览器这是一个Excel文件下载
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 设置文件名并防止中文乱码
        String fileName = URLEncoder.encode("客户列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 3. 查询需要导出的数据（带搜索条件）
        List<CustomerExportVo> list = customerService.listCustomersForExport(query);

        // 4. 使用 EasyExcel 将数据写入响应流
        EasyExcel.write(response.getOutputStream(), CustomerExportVo.class)
                .sheet("客户列表")
                .doWrite(list);
    }

    @Operation(summary = "导出选中的客户Excel")
    @PostMapping("/customer/export/selected")
    public void exportSelectedCustomers(@RequestBody List<Integer> ids, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("选中的客户列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 调用一个新的Service方法
        List<CustomerExportVo> list = customerService.listCustomersForExportByIds(ids);

        EasyExcel.write(response.getOutputStream(), CustomerExportVo.class)
                .sheet("选中的客户")
                .doWrite(list);
    }
}