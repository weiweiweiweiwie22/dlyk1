// 在 vo 包下创建 ClueRemarkVo.java
package com.weiwei.weidlykserver.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClueRemarkVo {
    private Integer id;
    private String noteWay; // 跟踪方式的文本，例如 "电话"
    private String noteContent;
    private LocalDateTime createTime;
    private String createBy; // 创建人的姓名
}