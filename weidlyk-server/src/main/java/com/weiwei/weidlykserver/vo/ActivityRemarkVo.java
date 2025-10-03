package com.weiwei.weidlykserver.vo;

import com.weiwei.weidlykserver.entity.ActivityRemark;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ActivityRemarkVo", description = "活动备注视图对象")
public class ActivityRemarkVo extends ActivityRemark {

    @Schema(description = "备注创建人姓名")
    private String createByName;
}