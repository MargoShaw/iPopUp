package com.margo.iPopUp.api.aspect;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.margo.iPopUp.api.support.UserSupport;
import com.margo.iPopUp.domain.User;
import com.margo.iPopUp.domain.annotation.LimitType;
import com.margo.iPopUp.domain.annotation.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;


/**
 * 限流处理
 *
 * @author margo
 */
@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    private StringRedisTemplate redisTemplate;

    private RedisScript<Long> limitScript;


    @Autowired
    private UserSupport userSupport;

    @Autowired
    public void setRedisTemplate1(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setLimitScript(RedisScript<Long> limitScript) {
        this.limitScript = limitScript;

    }


    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable {
        String key = getLimitUserIdKey(rateLimiter.key());
        Long curTime  = redisTemplate.getConnectionFactory().getConnection().time();
        int count = rateLimiter.count();
        int rate = rateLimiter.rate();
        String app = rateLimiter.app();
//        String combineKey = getCombineKey(rateLimiter, point);
        List<String> keys = Collections.singletonList(key);

        Boolean isExist = redisTemplate.hasKey(key);
        if(isExist==null || !isExist){
            Map<String,String> map = new HashMap<>();
            map.put("last_mill_second",curTime+"");
            map.put("curr_permits", 1+"");
            map.put("max_burst", count+"");
            map.put("rate", rate+"");
            map.put("app", app+"");
            redisTemplate.opsForHash().putAll(key, map);
        }
        curTime  = redisTemplate.getConnectionFactory().getConnection().time();
        try {

            Long number = redisTemplate.execute(limitScript, keys, "1", curTime+"");
            if (StringUtils.isEmpty(number+"") || number.intValue() == 0)
            {
                throw new Exception("访问过于频繁，请稍候再试");
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), key);
        }
        catch (Exception e) {
            throw new RuntimeException("服务器繁忙，开启限流！");
        }

    }

    public String getLimitUserIdKey(String key){
        return key + userSupport.getCurrentUserId();
    }

}
