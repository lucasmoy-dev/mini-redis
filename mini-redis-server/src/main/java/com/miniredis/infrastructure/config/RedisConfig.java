package com.miniredis.infrastructure.config;

import com.miniredis.application.services.RedisService;
import com.miniredis.domain.ports.in.RedisUseCase;
import com.miniredis.domain.ports.out.RedisRepository;
import com.miniredis.infrastructure.adapters.out.memory.InMemoryRedisRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class RedisConfig implements WebMvcConfigurer {

    private final RedisRepository repository = new InMemoryRedisRepository();

    @Bean
    public RedisRepository redisRepository() {
        return repository;
    }

    @Bean
    public RedisUseCase redisUseCase() {
        return new RedisService(repository);
    }

    @Scheduled(fixedRate = 1000)
    public void scheduleCleanup() {
        repository.cleanupExpired();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
