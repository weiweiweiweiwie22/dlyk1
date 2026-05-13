package com.weiwei.weidlykserver.query;

import lombok.Data;

@Data
public class TranQuery {
    private String tranNo;      // 交易流水号
    private Integer stage;      // 交易阶段
    private String customerName; // 客户姓名（模糊查询）
}