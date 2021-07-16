package com.sample.userservice.configs;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class UserServiceConfigs {

	/**
	 * Bean for ObjectMapper class, to be used for JSON Processing.
	 * 
	 * @return object for ObjectMapper with all the pre set properties.
	 */
	@SuppressWarnings("deprecation")
	@Bean("jacksonObjectMapper")
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper jacksonObjectMapper = new ObjectMapper();
		jacksonObjectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		jacksonObjectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		jacksonObjectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		return jacksonObjectMapper;
	}

	@Bean("webClientCommon")
	public WebClient webClientCommon() {
		HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.responseTimeout(Duration.ofMillis(5000))
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
		WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
		return client;

	}

	@Bean("webClientSSL")
	public WebClient webClientSSL() throws SSLException {
		SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();
		HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 5 seconds
				.responseTimeout(Duration.ofMillis(5000))
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)))
				.secure(t -> t.sslContext(sslContext));
		WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
		return client;
	}

	

}
