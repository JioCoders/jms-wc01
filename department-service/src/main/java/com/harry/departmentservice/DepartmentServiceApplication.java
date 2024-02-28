package com.harry.departmentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class DepartmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentServiceApplication.class, args);

		logger.info("****************-----App-Started-----****************");
		log.info("=======Tested OK=======");
	}

	// private static ConfigurableApplicationContext applicationContext;
	private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceApplication.class);

}
