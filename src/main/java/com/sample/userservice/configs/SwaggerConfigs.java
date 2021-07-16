package com.sample.userservice.configs;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigs implements WebFluxConfigurer {
	private static final String SWAGGER_UI_PATH = "/swagger-ui";

	@Bean
	public TaskExecutor threadPoolTaskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("default_task_executor_thread");
		executor.initialize();
		// Starting thread pool size is 1, core pool size is 5, max pool size is 10 and
		// the queue is 100. As requests come in, threads will be created up to 5 and
		// then tasks will be added to the queue until it reaches 100. When the queue is
		// full new threads will be created up to maxPoolSize

		// There is an interesting method allowCoreThreadTimeOut(boolean) which allows
		// core threads to be killed after given idle time. Setting this to true and
		// setting core threads = max threads allows the thread pool to scale between 0
		// and max threads.
		
		return executor;
	}

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).genericModelSubstitutes(Mono.class, Flux.class, Publisher.class)
				.select().apis(RequestHandlerSelectors.basePackage("com.xcomp.sample")).paths(PathSelectors.any())
				.build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler(SWAGGER_UI_PATH + "**").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler(SWAGGER_UI_PATH + "/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
