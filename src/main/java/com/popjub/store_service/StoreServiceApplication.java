package com.popjub.store_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.popjub.common.exception.GlobalExceptionHandler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@Import(GlobalExceptionHandler.class)
public class StoreServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreServiceApplication.class, args);
	}
}