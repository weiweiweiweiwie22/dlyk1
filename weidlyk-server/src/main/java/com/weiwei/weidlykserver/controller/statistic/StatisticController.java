package com.weiwei.weidlykserver.controller.statistic;

import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.StatisticService;
import com.weiwei.weidlykserver.vo.ChartDataVO;
import com.weiwei.weidlykserver.vo.StatisticVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "统计数据")
public class StatisticController {

    @Resource
    private StatisticService statisticService;
    @Operation(summary = "统计总数据")
    @GetMapping("/statistic/total")
    public Result<StatisticVO> total() {
        StatisticVO statisticVO = statisticService.total();
        return Result.ok(statisticVO);
    }

    @Operation(summary = "统计销售漏斗数据")
    @GetMapping("/statistic/funnel")
    public Result<List<ChartDataVO>> funnel() {
        List<ChartDataVO> funnel = statisticService.funnelData();
        return Result.ok(funnel);
    }

    @Operation(summary = "统计渠道数据")
    @GetMapping("/statistic/pie")
    public Result<List<ChartDataVO>> pie() {
        List<ChartDataVO> funnel = statisticService.sourceData();
        return Result.ok(funnel);
    }
}
