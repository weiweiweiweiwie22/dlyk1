package com.weiwei.weidlykserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;

import java.util.List;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface CustomerService extends IService<Customer> {

    IPage<CustomerPageVo> getCustomerPage(Page<CustomerPageVo> page, CustomerQuery query);

    List<CustomerExportVo> listCustomersForExport(CustomerQuery query);

    List<CustomerExportVo> listCustomersForExportByIds(List<Integer> ids);
}
