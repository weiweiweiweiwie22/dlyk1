package com.weiwei.weidlykserver.constant;

public class RedisConstant {
    public static final String ADMIN_LOGIN_PREFIX = "admin:login:";
    public static final Integer ADMIN_LOGIN_CAPTCHA_TTL_SEC = 60;

    public static final String APP_LOGIN_PREFIX = "app:login:";
    public static final Integer APP_LOGIN_CODE_RESEND_TIME_SEC = 60;
    public static final Integer APP_LOGIN_CODE_TTL_SEC = 60 * 10;
    public static final String APP_ROOM_PREFIX = "app:room:";

    // 定义JWT在Redis中的key前缀
    public static final String REDIS_JWT_KEY_PREFIX = "weidlyk:user:login:";

    //选择记住我后的jwt的过期时间（七天）
    public static final Long REDIS_JWT_TTL_SEC = 60 * 60 * 24 * 7L;

    //没有选择记住我后的jwt的过期时间(60分钟)
    public static final Long DEFAULT_REDIS_JWT_TTL_SEC = 60 * 60L;
}
