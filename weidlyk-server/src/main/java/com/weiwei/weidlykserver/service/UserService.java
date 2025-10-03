package com.weiwei.weidlykserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.dto.AddUserDto;
import com.weiwei.weidlykserver.dto.UpdateUserDto;
import com.weiwei.weidlykserver.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weiwei.weidlykserver.vo.UserVo;

import java.util.List;

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

    UserVo getDetailById(Integer id);

    int saveUser(AddUserDto user, Integer LoginUserId);

    boolean updateUser(UpdateUserDto updateUserDto, Integer editorId);

    boolean removeUserById(Integer id, Integer nowUserId);

    boolean removeBatchUsersByIds(Integer[] ids, Integer nowUserId);

    List<User> getlist();
}
