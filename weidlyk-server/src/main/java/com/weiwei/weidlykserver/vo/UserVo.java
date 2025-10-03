package com.weiwei.weidlykserver.vo;

import lombok.Data; // 使用 Lombok 简化代码
import java.time.LocalDateTime;

@Data // 自动生成 Getter, Setter, toString, equals, hashCode
public class UserVo {

    // 从 User 类中挑选前端需要的字段
    private Integer id;
    private String loginAct;
    private String name;
    private String phone;
    private String email;
    private Integer accountNoExpired;
    private Integer credentialsNoExpired;
    private Integer accountNoLocked;
    private Integer accountEnabled;
    private LocalDateTime createTime;
    private LocalDateTime editTime;
    private LocalDateTime lastLoginTime;

    // 新增的关联查询字段
    private String createByName;
    private String editByName;
    // 如果前端需要ID，也可以加上
    // private Integer createById;
    // private Integer editById;
}