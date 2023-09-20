package com.margo.iPopUp.domain.annotation;
import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter
{
    /**
     * 限流key
     */
    public String key() default "limit-key";

    /**
     * 限流时间,单位秒
     */
    public int time() default 60;

    /**
     * 令牌桶大小
     */
    public int count() default 100;

    /**
     * 限流类型
     */
    public String limitType() default LimitType.DEFAULT;

    /**
     * 令牌桶的令牌每秒生成速率
     * @return
     */
    public int rate() default 5;

    public String app() default "iPopUp";
}
