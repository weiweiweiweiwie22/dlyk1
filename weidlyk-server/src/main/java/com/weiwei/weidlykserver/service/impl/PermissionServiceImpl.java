package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.Permission;
import com.weiwei.weidlykserver.mapper.PermissionMapper;
import com.weiwei.weidlykserver.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public List<Permission> getMenusByUserId(Integer id) {
        return baseMapper.selectMenusByUserId(id);
    }
}
