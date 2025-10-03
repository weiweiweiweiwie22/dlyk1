// 在 vo 包下创建 CustomerExportVo.java
package com.weiwei.weidlykserver.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ColumnWidth(20) // 统一设置列宽
public class CustomerExportVo {

    @ExcelProperty("客户姓名")
    private String fullName;
    
    @ExcelProperty("手机号")
    private String phone;
    
    @ExcelProperty("负责人")
    private String ownerName;
    
    @ExcelProperty("意向产品")
    private String productName;
    
    @ExcelProperty("下次联系时间")
    private LocalDateTime nextContactTime;
    
    @ExcelProperty("成为客户时间")
    private LocalDateTime createTime;
}