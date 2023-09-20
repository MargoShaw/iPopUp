package com.margo.iPopUp.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author margoshaw
 * @date 2023/09/06 12:41
 **/
@Configuration
public class RedisConfig {
    @Bean("limitScript")
    public DefaultRedisScript<Long> limitScript()
    {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("ratelimit.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }




}
