package com.kch.api_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiProjectApplication.class, args);
	}

}
