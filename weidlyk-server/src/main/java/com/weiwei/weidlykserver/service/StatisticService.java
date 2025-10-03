package com.weiwei.weidlykserver.service;

import com.weiwei.weidlykserver.vo.ChartDataVO;
import com.weiwei.weidlykserver.vo.StatisticVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatisticService{
    /**
     * 统计总数据
     * @return 统计结果
     */
    StatisticVO total();

    List<ChartDataVO> funnelData();

    List<ChartDataVO> sourceData();
}
