package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
