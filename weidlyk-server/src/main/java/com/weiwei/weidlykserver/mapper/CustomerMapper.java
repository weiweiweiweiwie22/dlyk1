package com.weiwei.weidlykserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // <--- 1. 确保引入了这个包
import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 分页查询客户列表
     */
    // 2. 在 query 参数前添加 @Param("query") 注解
    IPage<CustomerPageVo> selectCustomerPage(Page<CustomerPageVo> page, @Param("query") CustomerQuery query);

    /**
     * 查询用于导出的客户列表
     */
    // 3. 在 query 参数前添加 @Param("query") 注解
    List<CustomerExportVo> selectCustomersForExport(@Param("query") CustomerQuery query);

    List<CustomerExportVo> selectCustomersForExportByIds(List<Integer> ids);
}