package com.sample.userservice.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;

import com.sample.userservice.exceptions.SomeThirdPartyException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@SuppressWarnings("rawtypes")





@Service("sampleFailureService")
@Slf4j
public class SampleFailureService {

	private static final String CIRCUIT_BREAKER_CACHE = "circuitBreakerCache";

	@Autowired
	CacheManager cacheManager;

	private final ReactiveCircuitBreaker sampleServiceCircuitBreaker;

	public SampleFailureService( ReactiveCircuitBreakerFactory cbFactory) {
		this.sampleServiceCircuitBreaker = cbFactory.create("sample-failure-service");
	}

	/**
	 * A method to generate random SomeThirdPartyException scenario, and this might
	 * be a random RestClientException as well in real time scenarios Resilience4J
	 * will be monitoring for the number of counts for this exception, and tend to
	 * change the state of the circuit as per specified configurations.
	 * 
	 * When the circuit is in open state the call goes to fallbackMethod.
	 * 
	 * 
	 * @return
	 * @throws SomeThirdPartyException
	 */
	public Mono<Integer> getSomeValue() {
		return this.sampleServiceCircuitBreaker.run(getRandomValue(), throwable -> {
			log.error("Some error occured", throwable.getMessage());
			return onFallbackReadFromCache();
		});
	}

	private Mono<Integer> getRandomValue() {
		Random random = new Random();
		Integer nextInteger = random.nextInt();
		if (nextInteger < 0) {
			return Mono.error(new SomeThirdPartyException("500.000.001", "Some random exception thrown"));
		}
		cacheManager.getCache(CIRCUIT_BREAKER_CACHE).put("lastSuccessValue", nextInteger);
		log.info("Cache updated");
		return Mono.just(nextInteger);
	}

	/**
	 * Executed only when circuit is opened. Hence no actual call goes within the
	 * method for a specified time limit. It tries to retrieve the previous
	 * generated value from the cache, and if cache is empty then it returns 0
	 * 
	 * @return Integer - random value
	 */
	private Mono<Integer> onFallbackReadFromCache() {
		log.info("Getting data from Cache");
		ValueWrapper previousGeneratedRandomNumber = cacheManager.getCache(CIRCUIT_BREAKER_CACHE)
				.get("lastSuccessValue");
		Integer cacheValue = (Integer) Optional.ofNullable(previousGeneratedRandomNumber).map(mapper -> mapper.get())
				.orElse(0);
		return Mono.just(cacheValue);
	}
}
