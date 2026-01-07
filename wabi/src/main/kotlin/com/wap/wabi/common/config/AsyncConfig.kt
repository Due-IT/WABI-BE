package com.wap.wabi.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean(name = ["taskExecutor"])
    fun taskExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()

        // 핵심 설정들
        executor.corePoolSize = 5        // 기본적으로 유지할 스레드 개수
        executor.maxPoolSize = 10        // 최대 생성 가능한 스레드 개수
        executor.queueCapacity = 500     // 대기 큐 크기
        executor.setThreadNamePrefix("Async-") // 로그에서 식별하기 위한 이름

        executor.initialize()
        return executor
    }
}