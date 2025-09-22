package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.service.UserLogin;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserLoginImpl implements UserLogin {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userMapper.selectByLoginAct(username);
       if (user == null){
           throw new UsernameNotFoundException("用户不存在");
       }
        return user;
    }
}
