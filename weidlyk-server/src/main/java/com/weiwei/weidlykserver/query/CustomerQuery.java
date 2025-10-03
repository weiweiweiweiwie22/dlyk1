// 在 query 包下创建 CustomerQuery.java
package com.weiwei.weidlykserver.query;

import lombok.Data;

@Data
public class CustomerQuery {
    private Integer ownerId;
    private Integer productId;
    private String fullName;
    private String phone;
}