package com.weiwei.weidlykserver.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(name = "CluePageVo", description = "线索分页列表视图对象")
public class CluePageVo {
    @Schema(description = "线索ID")
    private Integer id;
    @Schema(description = "负责人")
    private String ownerName;
    @Schema(description = "所属活动")
    private String activityName;
    @Schema(description = "姓名")
    private String fullName;
    @Schema(description = "称呼")
    private String appellation; // 字典值
    @Schema(description = "手机")
    private String phone;
    @Schema(description = "微信")
    private String wechat;
    @Schema(description = "是否贷款")
    private String needsLoan; // 字典值
    @Schema(description = "意向状态")
    private String intentionStatus; // 字典值
    @Schema(description = "意向产品")
    private String intentionProduct; // 字典值
    @Schema(description = "线索状态")
    private String status; // 字典值
    @Schema(description = "线索来源")
    private String source; // 字典值
    @Schema(description = "下次联系时间")
    private LocalDateTime nextContactTime;
}