package com.weiwei.weidlykserver.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "产品查询实体")
@Data
public class ProductQuery {

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "状态 0在售 1售罄")
    private Integer state;

}
