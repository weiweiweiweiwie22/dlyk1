// 在 service.impl 包下创建 CustomerServiceImpl.java
package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.mapper.CustomerMapper;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.service.CustomerService;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Override
    public IPage<CustomerPageVo> getCustomerPage(Page<CustomerPageVo> page, CustomerQuery query) {
        return baseMapper.selectCustomerPage(page, query);
    }

    // 在实现类中添加新方法
    @Override
    public List<CustomerExportVo> listCustomersForExport(CustomerQuery query) {
        return baseMapper.selectCustomersForExport(query);
    }

    @Override
    public List<CustomerExportVo> listCustomersForExportByIds(List<Integer> ids) {
        return baseMapper.selectCustomersForExportByIds(ids);
    }
}