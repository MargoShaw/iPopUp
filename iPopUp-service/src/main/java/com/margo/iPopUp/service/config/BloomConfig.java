package com.margo.iPopUp.service.config;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author margoshaw
 * @date 2023/08/01 17:00
 **/
@Configuration
public class BloomConfig {

        // @Bean
        // public BloomFilter<String> bloomFilter(){
        //     return BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000000, 0.01);
        // }

    @Bean("bloomFilter")
    public BloomFilter<String> bloomFilter(){
        return BloomFilter.create((from, into) -> into.putString(from, Charsets.UTF_8), 1000000, 0.01);
    }

}
