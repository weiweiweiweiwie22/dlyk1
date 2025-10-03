package com.weiwei.weidlykserver.constant;

public class RedisConstant {

    // 定义JWT在Redis中的key前缀
    public static final String REDIS_JWT_KEY_PREFIX = "weidlyk:user:login:";

    //选择记住我后的jwt的过期时间（七天）
    public static final Long REDIS_JWT_TTL_SEC = 60 * 60 * 24 * 7L;

    //没有选择记住我后的jwt的过期时间(60分钟)
    public static final Long DEFAULT_REDIS_JWT_TTL_SEC = 60 * 60L;

    // 用户列表
    public static final String REDIS_USER_LIST_KEY = "weidlyk:user:list:all";
}
