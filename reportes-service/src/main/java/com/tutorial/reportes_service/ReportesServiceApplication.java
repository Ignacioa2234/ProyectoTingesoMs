package com.tutorial.reportes_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ReportesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportesServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
