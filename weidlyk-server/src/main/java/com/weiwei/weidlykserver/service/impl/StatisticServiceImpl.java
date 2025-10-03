package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.weidlykserver.entity.Activity;
import com.weiwei.weidlykserver.mapper.ActivityMapper;
import com.weiwei.weidlykserver.mapper.ClueMapper;
import com.weiwei.weidlykserver.mapper.CustomerMapper;
import com.weiwei.weidlykserver.mapper.TranMapper;
import com.weiwei.weidlykserver.service.StatisticService;
import com.weiwei.weidlykserver.vo.ChartDataVO;
import com.weiwei.weidlykserver.vo.StatisticVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private ClueMapper clueMapper;

    @Resource
    private TranMapper tranMapper;
    @Override
    public StatisticVO total() {

        // 1. 获取当前服务器时间
        LocalDateTime now = LocalDateTime.now();
        // 2. 创建查询条件构造器
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        // 3. 添加查询条件：开始时间 <= 当前时间
        // le = Less than or Equal to
        queryWrapper.le(Activity::getStartTime, now);
        // 4. 添加查询条件：结束时间 >= 当前时间
        // ge = Greater than or Equal to
        queryWrapper.ge(Activity::getEndTime, now);

        // 有效市场活动数
        Long effectiveActivityCount = activityMapper.selectCount(queryWrapper);
        // 总市场活动数
        Long totalActivityCount = activityMapper.selectCount(null);
        // 线索总数
        Long totalClueCount = clueMapper.selectCount(null);
        // 客户总数
        Long totalCustomerCount = customerMapper.selectCount(null);
        // 成功交易金额
        BigDecimal successTranAmount = tranMapper.selectSuccessAmount();
        // 总交易金额
        BigDecimal totalTranAmount = tranMapper.selectTotalAmount();

        StatisticVO statisticVO = new StatisticVO();
        statisticVO.setEffectiveActivityCount(effectiveActivityCount);
        statisticVO.setTotalActivityCount(totalActivityCount);
        statisticVO.setTotalClueCount(totalClueCount);
        statisticVO.setTotalCustomerCount(totalCustomerCount);
        statisticVO.setSuccessTranAmount(successTranAmount);
        statisticVO.setTotalTranAmount(totalTranAmount);

        return statisticVO;
    }

    @Override
    public List<ChartDataVO> funnelData() {
        Long totalClueCount = clueMapper.selectCount(null);

        Long totalCustomerCount = customerMapper.selectCount(null);

        Long totalTranCount = tranMapper.selectCount(null);

        Long successTranCount = tranMapper.selectSuccessCount();

//        FunnelVO totalClue = new FunnelVO("线索", totalClueCount);
//        FunnelVO totalCustomer = new FunnelVO("客户", totalCustomerCount);
//        FunnelVO totalTran = new FunnelVO("交易", totalTranCount);
//        FunnelVO successTran = new FunnelVO("成交", successTranCount);

        ChartDataVO totalClue = ChartDataVO.builder()
                .name("线索")
                .value(totalClueCount)
                .build();

        ChartDataVO totalCustomer = ChartDataVO.builder()
                .name("客户")
                .value(totalCustomerCount)
                .build();
        ChartDataVO totalTran = ChartDataVO.builder()
                .name("交易")
                .value(totalTranCount)
                .build();
        ChartDataVO successTran = ChartDataVO.builder()
                .name("成交")
                .value(successTranCount)
                .build();

        return List.of(totalClue, totalCustomer, totalTran, successTran);
    }

    @Override
    public List<ChartDataVO> sourceData() {
        return clueMapper.selectSourceData();
    }
}
