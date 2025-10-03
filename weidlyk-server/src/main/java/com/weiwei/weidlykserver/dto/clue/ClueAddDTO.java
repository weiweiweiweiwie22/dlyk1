package com.weiwei.weidlykserver.dto.clue;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 线索添加DTO
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Data
public class ClueAddDTO {
    @Schema(description = "线索所属人ID")
    private Integer ownerId;

    @Schema(description = "活动ID")
    private Integer activityId;

    @Schema(description = "姓名")
    @TableField("full_name")
    private String fullName;

    @Schema(description = "称呼")
    @TableField("appellation")
    private Integer appellation;
}
