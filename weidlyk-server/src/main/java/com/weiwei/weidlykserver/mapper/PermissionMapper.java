package com.weiwei.weidlykserver.mapper;

import com.weiwei.weidlykserver.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据用户ID查询其所拥有的所有菜单权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectMenusByUserId(Integer userId);

    List<String> selectCodesByUserId(Integer userId);
}

