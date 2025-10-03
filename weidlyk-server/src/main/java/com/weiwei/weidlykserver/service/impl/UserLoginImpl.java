package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.mapper.PermissionMapper;
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.service.UserLogin;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// 建议直接实现 Spring Security 官方的 UserDetailsService 接口
public class UserLoginImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionMapper permissionMapper; // 确保注入了 PermissionMapper

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 先根据用户名查询用户基本信息
        User user = userMapper.selectByLoginAct(username);
        if (user == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        // 2. 根据用户ID查询该用户的角色列表
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        user.setRoleList(roles); // 填充 roleList

        // 3. 根据用户ID查询该用户的功能权限码列表
        List<String> permissions = permissionMapper.selectCodesByUserId(user.getId());
        user.setPermissionList(permissions); // 填充 permissionList

        // 4. 返回包含了完整角色和权限信息的 User 对象
        return user;
    }
}