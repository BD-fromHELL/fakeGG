package com.BDFH.fakeGG.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Retryer retryer() {
        // Retryer.Default(초기 간격(ms), 최대 간격(ms), 시도 횟수)
        // 시도가 진행 될때마다 이전 시행 간격 x 1.5의 간격을 두고 실행 (최대 간격까지)
        return new Retryer.Default(1000, 5000, 7);
    }
}
