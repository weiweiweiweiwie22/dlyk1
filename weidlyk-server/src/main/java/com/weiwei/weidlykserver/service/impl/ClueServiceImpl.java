package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Clue;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.mapper.ClueMapper;
import com.weiwei.weidlykserver.mapper.CustomerMapper;
import com.weiwei.weidlykserver.query.ClueQuery;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import com.weiwei.weidlykserver.service.ClueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.vo.ClueDetailVo;
import com.weiwei.weidlykserver.vo.CluePageVo;
import com.weiwei.weidlykserver.vo.ClueRemarkVo;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * <p>
 * 线索表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements ClueService {
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private ClueMapper clueMapper;

    @Override
    public IPage<CluePageVo> getCluePage(Page<Clue> page, ClueQuery query) {
        return baseMapper.selectCluePage(page, query);
    }

    @Override
    public boolean isPhoneUnique(String phone) {
        if (!StringUtils.hasText(phone)) {
            // 如果手机号为空，我们认为它是“唯一”的，让前端的“必填”验证去处理空值情况
            return true;
        }
        // 使用MyBatis-Plus的LambdaQueryWrapper查询数据库中是否存在该手机号
        LambdaQueryWrapper<Clue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clue::getPhone, phone);

        // count() > 0 意味着找到了至少一条记录，所以手机号不是唯一的
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public ClueDetailVo getClueDetailById(Long id) {
        // 1. 先查询线索主体详情
        ClueDetailVo clueDetailVo = baseMapper.selectDetailById(id);

        // 如果线索不存在，直接返回 null
        if (clueDetailVo == null) {
            return null;
        }

        // 2. 根据线索ID，查询其关联的备注列表
        List<ClueRemarkVo> remarkList = baseMapper.selectRemarkListByClueId(id);

        // 3. 将备注列表设置到详情VO中
        clueDetailVo.setRemarkList(remarkList);

        // 4. 返回组合好的完整数据
        return clueDetailVo;
    }

    @Override
    public boolean saveClue(Clue clue, Integer userId) {
        clue.setCreateBy(userId);
        clue.setCreateTime(LocalDateTime.now());
        return save(clue);

    }

    @Override
    @Transactional
    // 1. 修改方法签名，接收 userId
    public boolean updateClue(Clue clue, Integer userId) {
        // 2. (重要) 更新审计字段，使用传递过来的 userId
        clue.setEditBy(userId);
        clue.setEditTime(LocalDateTime.now()); // 或 ZonedDateTime.now()

        // 3. 执行更新
        return this.updateById(clue);
    }

    @Transactional
    @Override
    public boolean convertToCustomer(Customer customer, Integer currentUserId) {
        Clue clue = clueMapper.selectById(customer.getClueId());
        if (clue.getState() == -1) {
            throw new BusinessException(ResultCodeEnum.CONVERT_ERROR);
        }
        clue.setState(-1);
        clueMapper.updateById(clue);
        customer.setCreateBy(currentUserId);
        customer.setCreateTime(LocalDateTime.now());

        return customerMapper.insert( customer) > 0;
    }

}
