package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.Product;
import com.weiwei.weidlykserver.mapper.ProductMapper;
import com.weiwei.weidlykserver.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
