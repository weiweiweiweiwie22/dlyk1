package com.weiwei.weidlykserver.vo;

import java.math.BigDecimal;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
@lombok.ToString
public class StatisticVO {

    //有效市场活动数
    private Long effectiveActivityCount;
    //总市场活动数
    private Long totalActivityCount;
    //线索总数
    private Long totalClueCount;
    //客户总数
    private Long totalCustomerCount;
    //成功交易金额
    private BigDecimal successTranAmount;
    //总交易金额
    private BigDecimal totalTranAmount;
}
