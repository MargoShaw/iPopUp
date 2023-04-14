package com.margo.iPopUp.service.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author margoshaw
 * @date 2023/04/14 20:57
 **/
@Configuration
public class AsyncMySqlConfig   {
    @Value("${executor.io.corePoolSize}")
    private  int corePoolSize;

    @Value("${executor.io.maxPoolSize}")
    private  int maxPoolSize;

    @Value("${executor.io.queueCapacity}")
    private  int queueCapacity;

    @Value("${executor.io.keepAliveSeconds}")
    private int keepAliveSeconds;

    @Bean("io-executor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);

        executor.setThreadNamePrefix("margo-io-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
