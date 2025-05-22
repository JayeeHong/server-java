package kr.hhplus.be.server.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "orderEventExecutor")
    public Executor orderEventExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(2); // 최소 스레드 갯수
        exec.setMaxPoolSize(5); // 최대 스레드 갯수
        exec.setQueueCapacity(50); // 대기열 큐 크기
        exec.setThreadNamePrefix("OrderEvent.");
        exec.initialize();
        return exec;
    }
}
