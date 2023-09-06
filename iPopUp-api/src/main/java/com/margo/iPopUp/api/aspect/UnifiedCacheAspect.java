package com.margo.iPopUp.api.aspect;

import com.alibaba.fastjson.JSON;
import com.margo.iPopUp.api.support.UserSupport;
import com.margo.iPopUp.domain.JsonResponse;
import com.margo.iPopUp.domain.annotation.UnifiedCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author margoshaw
 * @date 2023/04/15 00:10
 **/
@Order(1)
@Component
@Aspect
public class UnifiedCacheAspect {


    @Pointcut("@annotation(com.margo.iPopUp.domain.annotation.UnifiedCache)")
    public void check(){
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Around("check()")
    public Object around(ProceedingJoinPoint pjp){
        try{
        Signature signature=pjp.getSignature();
        String methodName = signature.getName();
        String className = pjp.getTarget().getClass().getSimpleName();
        Class[] parameterTypes = new Class[pjp.getArgs().length];
        Object[] args = pjp.getArgs();
        StringBuilder params=new StringBuilder();
        for(int i=0; i<args.length; i++) {
            if(args[i] != null) {
                params.append(JSON.toJSONString(args[i]));
                parameterTypes[i] = args[i].getClass();
            }else {
                parameterTypes[i] = null;
            }
        }
        if (StringUtils.isNotEmpty(params)) {
            //加密 以防出现key过长以及字符转义获取不到的情况
            params = new StringBuilder(DigestUtils.md5Hex(params.toString()));
        }
        Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
        //获取Cache注解
        UnifiedCache annotation = method.getAnnotation(UnifiedCache.class);
        //缓存过期时间
        long expire = annotation.expire();
        //缓存名称
        String name = annotation.name();
        //先从redis获取ee
        String redisKey = name + "::" + className+"::"+methodName+"::"+params.toString();
        String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)){
                JsonResponse jsonResponse = JSON.parseObject(redisValue, JsonResponse.class);
                return jsonResponse;
            }
        Object proceed = pjp.proceed();
        redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
        return proceed;
    } catch (Throwable throwable) {
        throwable.printStackTrace();
    }
        return JsonResponse.fail("555","系统错误");
    }
}
