package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.Tran;
import com.weiwei.weidlykserver.entity.TranHistory;
import com.weiwei.weidlykserver.mapper.TranMapper;
import com.weiwei.weidlykserver.mapper.TranHistoryMapper;
import com.weiwei.weidlykserver.service.TranService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TranServiceImpl extends ServiceImpl<TranMapper, Tran> implements TranService {

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTran(Tran tran) {
        // 1. 生成唯一的交易流水号
        tran.setTranNo(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        tran.setCreateTime(LocalDateTime.now());

        // 2. 保存交易主表
        int result = baseMapper.insert(tran);

        // 3. 记录初始阶段历史
        if (result > 0) {
            TranHistory history = new TranHistory();
            history.setTranId(tran.getId());
            history.setStage(tran.getStage());
            history.setMoney(tran.getMoney());
            history.setExpectedLocalDateTime(tran.getExpectedLocalDateTime());
            history.setCreateTime(LocalDateTime.now());
            history.setCreateBy(tran.getCreateBy());
            tranHistoryMapper.insert(history);
        }
        return result > 0;
    }

    /**
     * 推进交易阶段，并记录变动快照
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTranStage(Tran tran) {
        // 1. 更新交易主表信息（MyBatis-Plus会自动处理传入的 stage, money, expectedLocalDateTime 等非空字段）
        tran.setEditTime(LocalDateTime.now());
        // 注意：editBy 是在 Controller 层通过 Authentication 获取并塞入 tran 对象的
        int result = baseMapper.updateById(tran);

        // 2. 产生一条新的阶段流转历史记录
        if (result > 0) {
            // 【核心优化】：重新查询主表，确保历史表记录的是主表落库后的最新、最完整数据
            Tran dbTran = baseMapper.selectById(tran.getId());

            TranHistory history = new TranHistory();
            history.setTranId(dbTran.getId());
            history.setStage(dbTran.getStage());
            history.setMoney(dbTran.getMoney()); // 完美记录变动后的金额
            history.setExpectedLocalDateTime(dbTran.getExpectedLocalDateTime()); // 记录变动后的预计成交日期

            history.setCreateTime(LocalDateTime.now());
            history.setCreateBy(tran.getEditBy());

            tranHistoryMapper.insert(history);
        }
        return result > 0;
    }
}