package com.weiwei.weidlykserver.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ClueQuery", description = "线索分页查询参数")
public class ClueQuery {
    @Schema(description = "负责人ID")
    private Integer ownerId;

    @Schema(description = "所属活动ID")
    private Integer activityId;

    @Schema(description = "客户姓名")
    private String fullName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "线索状态")
    private String status;

    @Schema(description = "线索来源")
    private String source;
}