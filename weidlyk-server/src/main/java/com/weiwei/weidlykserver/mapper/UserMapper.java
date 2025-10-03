package com.weiwei.weidlykserver.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.common.DataScope;
import com.weiwei.weidlykserver.dto.AddUserDto;
import com.weiwei.weidlykserver.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiwei.weidlykserver.vo.UserVo;

import java.util.List;

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

    @DataScope(tableAlias = "tu", tableField = "id")
    IPage<User> pageItem(IPage<User> page);

    UserVo getDetailById(Integer id);
    /**
     * 保存用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int saveUser(User user);

    /**
     * 根据用户ID查询角色标识列表
     * @param userId 用户ID
     * @return 角色标识字符串列表
     */
    List<String> selectRolesByUserId(Integer userId);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);
}
