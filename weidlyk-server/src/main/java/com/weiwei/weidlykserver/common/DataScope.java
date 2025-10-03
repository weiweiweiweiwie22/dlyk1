package com.weiwei.weidlykserver.common;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * @author weiwei
 * @date 2025/9/22
 */
@Target(ElementType.METHOD) // 定义这个注解可以用在方法上
@Retention(RetentionPolicy.RUNTIME) // 定义这个注解在运行时可见
@Documented
public @interface DataScope {
    //在sql语句末尾添加一个 where 子句，用于限制数据权限
    //select * from user
    //select * from user where user_id = 2

    /**
     * 表别名
     * @return 表别名
     */
    public String tableAlias() default "";

     /**
      * 数据权限字段
      * @return 数据权限字段
      */
    public String tableField() default "";



}
