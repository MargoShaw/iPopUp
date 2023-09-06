package com.margo.iPopUp.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 限流脚本
     */
    private String limitScriptText()
    {
        return  "local ratelimit_info=redis.pcall(\"HMGET\",KEYS[1],\"last_mill_second\",\"curr_permits\",\"max_burst\",\"rate\",\"app\")\n" +
                "local last_mill_second=ratelimit_info[1]\n" +
                "local curr_permits=tonumber(ratelimit_info[2])\n" +
                "local max_burst=tonumber(ratelimit_info[3])\n" +
                "local rate=tonumber(ratelimit_info[4])\n" +
                "local app=tostring(ratelimit_info[5])\n" +
                "if app == nil then\n" +
                "\treturn 0\n" +
                "end\n" +
                "\n" +
                "local local_curr_permits=max_burst;\n" +
                "\n" +
                "if(type(last_mill_second) ~='boolean' and last_mill_second ~=nil) then\n" +
                "\tlocal reverse_permits=math.floor((ARGV[2]-last_mill_second)/1000)*rate\n" +
                "\tif(reverse_permits>0) then\n" +
                "\t\tredis.pcall(\"HMSET\",KEYS[1],\"last_mill_second\",ARGV[2])\n" +
                "\tend\n" +
                "\n" +
                "\tlocal expect_curr_permits=reverse_permits+curr_permits\n" +
                "\tlocal_curr_permits=math.min(expect_curr_permits,max_burst);\n" +
                "\n" +
                "else\n" +
                "\tredis.pcall(\"HMSET\",KEYS[1],\"last_mill_second\",ARGV[2])\n" +
                "end\n" +
                "\n" +
                "local result=-1\n" +
                "if(local_curr_permits-ARGV[1]>0) then\n" +
                "\tresult=1\n" +
                "\tredis.pcall(\"HMSET\",KEYS[1],\"curr_permits\",local_curr_permits-ARGV[1])\n" +
                "else\n" +
                "\tredis.pcall(\"HMSET\",KEYS[1],\"curr_permits\",local_curr_permits)\n" +
                "end\n" +
                "\n" +
                "return result";
    }
}
