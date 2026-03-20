package com.weiwei.weidlykserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Tran;
import com.weiwei.weidlykserver.query.TranQuery;
import com.weiwei.weidlykserver.vo.TranDetailVo;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface TranMapper extends BaseMapper<Tran> {

    BigDecimal selectSuccessAmount();

    BigDecimal selectTotalAmount();

    Long selectSuccessCount();

    // 声明自定义分页查询方法，MyBatis 会自动寻找 XML 中的 selectTranPage
    IPage<Tran> selectTranPage(@Param("page") Page<Tran> page, @Param("query") TranQuery query);

    TranDetailVo selectTranDetailById(Integer id);
}