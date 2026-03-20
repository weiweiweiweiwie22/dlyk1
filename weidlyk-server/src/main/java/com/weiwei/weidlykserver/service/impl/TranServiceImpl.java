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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTranStage(Tran tran) {
        // 1. 更新交易主表的阶段和编辑信息
        tran.setEditTime(LocalDateTime.now());
        int result = baseMapper.updateById(tran);

        // 2. 产生一条新的阶段流转历史
        if (result > 0) {
            TranHistory history = new TranHistory();
            history.setTranId(tran.getId());
            history.setStage(tran.getStage());
            history.setMoney(tran.getMoney());
            history.setExpectedLocalDateTime(tran.getExpectedLocalDateTime());
            history.setCreateTime(LocalDateTime.now());
            history.setCreateBy(tran.getEditBy()); // 变更历史的创建人即为当前编辑人
            tranHistoryMapper.insert(history);
        }
        return result > 0;
    }
}