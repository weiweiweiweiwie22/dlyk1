// 在 service.impl 包下创建 CustomerServiceImpl.java
package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.mapper.CustomerMapper;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.CustomerService;
import com.weiwei.weidlykserver.vo.CustomerDetailVo;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
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

    @Override
    public CustomerDetailVo getCustomerDetailById(Integer id) {
        // 调用 baseMapper 中我们自定义的 selectDetailById 方法
        // 这个方法会自动处理 t_customer, t_clue, t_user 的多表联查
        return baseMapper.selectDetailById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomer(Customer customer, Integer userId) {
        // 设置审计字段：记录是谁在什么时候改的
        customer.setEditBy(userId);
        customer.setEditTime(LocalDateTime.now());

        // MyBatis-Plus 的 updateById 会提取 customer 对象里的 id 作为 WHERE 条件
        // 其余非空字段作为 SET 内容
        return this.updateById(customer);
    }
}