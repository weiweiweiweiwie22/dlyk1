package com.weiwei.weidlykserver.mapper;

import com.weiwei.weidlykserver.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByLoginAct(String username);
}
