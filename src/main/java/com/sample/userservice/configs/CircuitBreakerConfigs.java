package com.sample.userservice.configs;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.userservice.exceptions.SomeThirdPartyException;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class CircuitBreakerConfigs {

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
															.slidingWindowSize(5)
															.permittedNumberOfCallsInHalfOpenState(5)
															.failureRateThreshold(50)
															.minimumNumberOfCalls(5)
															.waitDurationInOpenState(Duration.ofSeconds(5))
															.recordExceptions(SomeThirdPartyException.class)
															.automaticTransitionFromOpenToHalfOpenEnabled(true)
															.build();
		return factory -> {
			factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
												.circuitBreakerConfig(circuitBreakerConfig)
												.timeLimiterConfig(TimeLimiterConfig.custom()
																		.timeoutDuration(Duration.ofSeconds(4))
																		.build())
												.build());
			factory.addCircuitBreakerCustomizer(
					circuitBreaker -> circuitBreaker.getEventPublisher().onStateTransition(e -> {
						switch (e.getStateTransition().getToState()) {
						case CLOSED:
							log.info("Circuit Breaker is now CLOSED.");
							break;
						case HALF_OPEN:
							log.info("Circuit Breaker is now HALF_OPEN.");
							break;
						case OPEN:
							log.info("Circuit Breaker is now OPEN!");
							break;
						case METRICS_ONLY:
							break;
						default:
							break;
						}
					}), "circuitBreakerStateTransitionEvents");
		};

	}

	@SuppressWarnings("rawtypes")
	
	
	@Bean
	public ReactiveCircuitBreakerFactory circuitBreakerFactory() {
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
															.slidingWindowSize(5)
															.permittedNumberOfCallsInHalfOpenState(5)
															.failureRateThreshold(50)
															.minimumNumberOfCalls(5)
															.waitDurationInOpenState(Duration.ofSeconds(5))
															.recordExceptions(SomeThirdPartyException.class)
															.automaticTransitionFromOpenToHalfOpenEnabled(true)
															.build();
		CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
		ReactiveResilience4JCircuitBreakerFactory cbFactory = new ReactiveResilience4JCircuitBreakerFactory();
		cbFactory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig), "sample-failure-service");
		cbFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
		cbFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
												.timeLimiterConfig(TimeLimiterConfig.custom()
																				.timeoutDuration(Duration.ofSeconds(4))
																				.build())
												.circuitBreakerConfig(circuitBreakerConfig)
												.build());
		cbFactory.addCircuitBreakerCustomizer(
				circuitBreaker -> circuitBreaker.getEventPublisher().onStateTransition(e -> {
					switch (e.getStateTransition().getToState()) {
					case CLOSED:
						log.info("Circuit Breaker is now CLOSED.");
						break;
					case HALF_OPEN:
						log.info("Circuit Breaker is now HALF_OPEN.");
						break;
					case OPEN:
						log.info("Circuit Breaker is now OPEN!");
						break;
					case METRICS_ONLY:
						break;
					default:
						break;
					}
				}), "circuitBreakerStateTransitionEvents");

		return cbFactory;
	}
}
