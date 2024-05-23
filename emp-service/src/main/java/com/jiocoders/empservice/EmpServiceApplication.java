package com.jiocoders.empservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @EnableWebFlux
// @EntityScan(basePackages = { "com.jiocoders.empservice.entity" }) // scan JPA
// entities
// @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
// HibernateJpaAutoConfiguration.class })
@SpringBootApplication
@EnableConfigurationProperties
public class EmpServiceApplication extends TaskRunnable {

	public static void main(String[] args) {
		SpringApplication.run(EmpServiceApplication.class, args);
		log.info("=======Application has been started. OK=======");
	}
}
