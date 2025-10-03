package com.weiwei.weidlykserver.dto.clue;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClueImportDTO {
    @ExcelProperty("负责人")
    @NotBlank(message = "负责人不能为空")
    private String owner;

    @ExcelProperty("所属活动")
    private String activityName;

    @ExcelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String fullName;

    @ExcelProperty("称呼")
    private String appellation;

    @ExcelProperty("手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ExcelProperty("微信号")
    private String wechat;

    @ExcelProperty("QQ号")
    private String qq;

    @ExcelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("职业")
    private String job;

    @ExcelProperty("年收入")
    private String annualIncome;

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("是否贷款")
    private String isLoan;

    @ExcelProperty("意向状态")
    private String intentionState;

    @ExcelProperty("意向产品")
    private String intentionProduct;

    @ExcelProperty("线索状态")
    private String clueState;

    @ExcelProperty("线索来源")
    private String source;

    @ExcelProperty("线索描述")
    private String description;

    @ExcelProperty("下次联系时间")
    private LocalDateTime nextContactTime;
}