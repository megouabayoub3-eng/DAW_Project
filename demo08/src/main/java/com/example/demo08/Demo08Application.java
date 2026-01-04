package com.example.demo08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo08Application {

	private static final Logger log = LoggerFactory.getLogger(Demo08Application.class);

	public static void main(String[] args) {
		var ctx = SpringApplication.run(Demo08Application.class, args);
		log.info("Application started successfully. Active profiles: {}",
				String.join(",", ctx.getEnvironment().getActiveProfiles()));
	}

}
