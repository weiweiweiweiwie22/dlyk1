package com.weiwei.weidlykserver.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

// ClueDetailVo.java
@Data
public class ClueDetailVo {
    // 包含所有Clue实体类的字段
    private Integer id;
    private Integer ownerId;
    private Integer activityId;
    private String fullName;
    private String phone;
    private String weixin;
    private Integer age;
    private String job;
    private BigDecimal yearIncome;
    private String address;
    private String description;
    private LocalDateTime nextContactTime; // 假设你已采纳之前的建议
    private LocalDateTime createTime;
    private LocalDateTime editTime;

    // 新增的、用于前端展示的文本字段
    private String ownerName;
    private String activityName;
    private String appellation; // 从ID转换成 "先生" 等
    private String needLoan;    // 从ID转换成 "是/否" 等
    private String intentionState;
    private String intentionProduct;
    private String state;
    private String source;
    private String createBy;
    private String editBy;

    private List<ClueRemarkVo> remarkList;
}