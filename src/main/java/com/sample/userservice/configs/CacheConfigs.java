package com.sample.userservice.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CacheConfigs {

	private static final String CIRCUIT_BREAKER_CACHE = "circuitBreakerCache";

	@Bean
	CacheManager cacheManager() {
		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(CIRCUIT_BREAKER_CACHE);
		return cacheManager;
	}

	@CacheEvict(allEntries = true, value = { CIRCUIT_BREAKER_CACHE })
	@Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 1 * 60 * 1000) // 24hrs fixed delay.
	public void evictCircuitBreakerCache() {
		log.info("Cache eviction performed.");
	}

}
