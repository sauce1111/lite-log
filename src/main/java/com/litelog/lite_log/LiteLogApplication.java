package com.litelog.lite_log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LiteLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteLogApplication.class, args);
	}

}
