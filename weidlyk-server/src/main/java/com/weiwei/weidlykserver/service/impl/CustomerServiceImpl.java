package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.mapper.CustomerMapper;
import com.weiwei.weidlykserver.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
