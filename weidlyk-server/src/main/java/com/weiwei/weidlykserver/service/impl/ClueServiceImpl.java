package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Clue;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.entity.Tran;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.mapper.ClueMapper;
import com.weiwei.weidlykserver.mapper.CustomerMapper;
import com.weiwei.weidlykserver.query.ClueQuery;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import com.weiwei.weidlykserver.service.ClueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.service.TranService;
import com.weiwei.weidlykserver.vo.ClueDetailVo;
import com.weiwei.weidlykserver.vo.CluePageVo;
import com.weiwei.weidlykserver.vo.ClueRemarkVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    // 【新增】：注入交易服务，用于生成初始交易
    @Resource
    private TranService tranService;

    @Override
    public IPage<CluePageVo> getCluePage(Page<Clue> page, ClueQuery query) {
        return baseMapper.selectCluePage(page, query);
    }

    @Override
    public boolean isPhoneUnique(String phone) {
        if (!StringUtils.hasText(phone)) {
            return true;
        }
        LambdaQueryWrapper<Clue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clue::getPhone, phone);
        return baseMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public ClueDetailVo getClueDetailById(Long id) {
        ClueDetailVo clueDetailVo = baseMapper.selectDetailById(id);
        if (clueDetailVo == null) {
            return null;
        }
        List<ClueRemarkVo> remarkList = baseMapper.selectRemarkListByClueId(id);
        clueDetailVo.setRemarkList(remarkList);
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
    public boolean updateClue(Clue clue, Integer userId) {
        clue.setEditBy(userId);
        clue.setEditTime(LocalDateTime.now());
        return this.updateById(clue);
    }

    /**
     * 线索转换客户逻辑闭环
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean convertToCustomer(Customer customer, Integer currentUserId) {
        // 1. 检查线索状态，防止重复转换
        Clue clue = clueMapper.selectById(customer.getClueId());
        if (clue == null || clue.getState() == -1) {
            throw new BusinessException(ResultCodeEnum.CONVERT_ERROR);
        }

        // 2. 更新线索状态为“已转客户” (-1)
        clue.setState(-1);
        clueMapper.updateById(clue);

        // 3. 完善客户基础信息并插入
        customer.setCreateBy(currentUserId);
        customer.setCreateTime(LocalDateTime.now());
        int result = customerMapper.insert(customer);

        // 4. 【核心闭环】：如果前端选择了意向产品，则自动创建一笔交易
        if (result > 0 && customer.getProduct() != null) {
            Tran tran = new Tran();
            tran.setCustomerId(customer.getId()); // 关联新生成的客户ID
            tran.setMoney(BigDecimal.ZERO);      // 初始金额设为0，待销售后续修改
            tran.setStage(1);                     // 设置为初始阶段（如：资质审查）
            tran.setDescription(customer.getDescription()); // 沿用转换时的描述
            tran.setExpectedLocalDateTime(customer.getNextContactTime()); // 以“下次联系时间”作为预计成交日期

            tran.setCreateBy(currentUserId);
            tran.setCreateTime(LocalDateTime.now());

            // 调用我们之前实现的 saveTran，会自动处理交易号和历史记录
            tranService.saveTran(tran);
        }

        return result > 0;
    }
}