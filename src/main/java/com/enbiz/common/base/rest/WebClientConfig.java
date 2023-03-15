package com.enbiz.common.base.rest;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	
	@Value("${enbiz.web-client.connectTimeout:5}")
	private long connectTimeout;

	@Value("${enbiz.web-client.readTimeout:30}")
	private long readTimeout;

	@Bean
	public WebClient webClient() {

		HttpClient httpClient = HttpClient.create()
		        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int)Duration.ofSeconds(connectTimeout).toMillis()) // connection timeout
		        .responseTimeout(Duration.ofSeconds(readTimeout)); // response timeout

		WebClient webClient = WebClient.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();

		return webClient;
		
	}

}
