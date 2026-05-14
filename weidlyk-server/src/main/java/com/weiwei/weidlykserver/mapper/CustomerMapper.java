package com.weiwei.weidlykserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.vo.CustomerDetailVo;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import com.weiwei.weidlykserver.vo.CustomerPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 分页查询客户列表
     */
    IPage<CustomerPageVo> selectCustomerPage(Page<CustomerPageVo> page, @Param("query") CustomerQuery query);

    /**
     * 查询用于导出的客户列表
     */
    List<CustomerExportVo> selectCustomersForExport(@Param("query") CustomerQuery query);

    // 👇 【核心修改】：在这里加上 @Param("ids")
    List<CustomerExportVo> selectCustomersForExportByIds(@Param("ids") List<Integer> ids);

    // 对应 XML 中的 <select id="selectDetailById">
    CustomerDetailVo selectDetailById(Integer id);
}