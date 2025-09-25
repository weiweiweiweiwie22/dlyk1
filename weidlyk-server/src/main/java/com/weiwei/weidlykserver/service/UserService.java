package com.weiwei.weidlykserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface UserService extends IService<User> {

    IPage<User> pageItem(IPage<User> page);
}
