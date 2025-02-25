package com.litelog.lite_log;

import com.litelog.lite_log.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class LiteLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteLogApplication.class, args);
	}

}
