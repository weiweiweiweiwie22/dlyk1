package com.weiwei.weidlykserver.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.weidlykserver.entity.Permission;
import com.weiwei.weidlykserver.entity.RolePermission;
import com.weiwei.weidlykserver.mapper.PermissionMapper;
import com.weiwei.weidlykserver.mapper.RolePermissionMapper;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductMenuInit implements CommandLineRunner {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getUrl, "/dashboard/product");
        Permission exist = permissionMapper.selectOne(wrapper);
        if (exist == null) {
            Permission productMenu = new Permission();
            productMenu.setName("产品管理");
            productMenu.setCode("product:menu");
            productMenu.setUrl("/dashboard/product");
            productMenu.setType("menu");
            productMenu.setParentId(0); // 挂在根目录或者根据你的需求修改
            productMenu.setOrderNo(10);
            productMenu.setIcon("Goods");
            permissionMapper.insert(productMenu);

            // 给角色1(通常是管理员)分配权限
            RolePermission rp = new RolePermission();
            rp.setRoleId(1);
            rp.setPermissionId(productMenu.getId());
            rolePermissionMapper.insert(rp);
            
            System.out.println("成功初始化 产品管理 菜单和管理员权限！");
        }
    }
}
