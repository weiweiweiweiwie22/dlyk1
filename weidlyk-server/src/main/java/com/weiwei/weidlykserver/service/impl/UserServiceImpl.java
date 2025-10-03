package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.constant.RedisConstant;
import com.weiwei.weidlykserver.dto.AddUserDto;
import com.weiwei.weidlykserver.dto.UpdateUserDto;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import com.weiwei.weidlykserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.util.JWTUtils;
import com.weiwei.weidlykserver.vo.UserVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RedisTemplate<String , Object> redisTemplate;
    @Override
    public IPage<User> pageItem(IPage<User> page) {

        return userMapper.pageItem( page);
    }

    @Override
    public UserVo getDetailById(Integer id) {
        return userMapper.getDetailById(id);
    }

    @Override
    public int saveUser(AddUserDto userDto, Integer LoginUserId) {

        User user = new User();
        BeanUtils.copyProperties(userDto,user);

        // 创建时间
        user.setCreateTime(LocalDateTime.now());

        // 密码加密
        user.setLoginPwd(passwordEncoder.encode(user.getLoginPwd()));

        user.setCreateBy(LoginUserId);

        int result = userMapper.saveUser(user);
        if (result > 0) {
            // 新增成功后，删除缓存
            redisTemplate.delete(RedisConstant.REDIS_USER_LIST_KEY);
        }
        return result;
    }

    @Override
    public boolean updateUser(UpdateUserDto updateUserDto, Integer editorId) {
        User user = userMapper.selectById(updateUserDto.getId());
        if (user == null){
            return false;
        }
        BeanUtils.copyProperties(updateUserDto,user);
        user.setEditTime(LocalDateTime.now());
        user.setEditBy(editorId);
        boolean result = userMapper.updateById(user) > 0;
        if (result) {
            // 更新成功后，删除缓存
            redisTemplate.delete(RedisConstant.REDIS_USER_LIST_KEY);
        }
        return result;
    }

    @Override
    public boolean removeUserById(Integer id, Integer nowUserId) {
        // 不能删除自己
        if(Objects.equals(id, nowUserId)){
            throw new BusinessException(ResultCodeEnum.USER_DELETE_SELF_ERROR);
        }
        boolean result = userMapper.deleteById(id) > 0;
        if (result) {
            // ⬇️⬇️⬇️ 在这里补充删除缓存的逻辑 ⬇️⬇️⬇️
            redisTemplate.delete(RedisConstant.REDIS_USER_LIST_KEY);
        }
        return result;
    }

    @Override
    public boolean removeBatchUsersByIds(Integer[] ids, Integer nowUserId) {
        // 1. 检查是否包含当前用户ID
        for (Integer id : ids) {
            if (Objects.equals(id, nowUserId)) {
                throw new BusinessException(ResultCodeEnum.USER_DELETE_SELF_ERROR);
            }
        }

        int count = 0;
        if (ids.length > 0) {
            count = baseMapper.deleteBatchIds(Arrays.asList(ids));
        }

        boolean result = count > 0;
        if (result) {
            // ⬇️⬇️⬇️ 在这里补充删除缓存的逻辑 ⬇️⬇️⬇️
            redisTemplate.delete(RedisConstant.REDIS_USER_LIST_KEY);
        }
        return result;
    }

    @Override
    public List<User> getlist() {
        String key = "weidlyk:user:list:all";
        if (redisTemplate.hasKey(key)) {
            return (List<User>) redisTemplate.opsForValue().get(key);
        }
        List<User> userList = userMapper.selectList(null);
        redisTemplate.opsForValue().set(key, userList);
        return userList;
    }
}
