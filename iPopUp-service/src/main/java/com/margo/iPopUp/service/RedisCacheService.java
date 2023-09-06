package com.margo.iPopUp.service;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author margoshaw
 * @date 2023/08/01 17:03
 **/
@Service
public class RedisCacheService {

    @Autowired
    private BloomFilter<String> bloomFilter;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean isExist(String key){
        return bloomFilter.mightContain(key);
    }
    public Object getFromCache(String key){
        if(!isExist(key)){
            return null;
        }

        return redisTemplate.opsForValue().get(key);
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void rebuildBloomFilter(){
        bloomFilter = BloomFilter.create((from, into) -> into.putString(from, Charsets.UTF_8), 1000000, 0.01);

    }
}
