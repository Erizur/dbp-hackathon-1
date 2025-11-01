package com.example.oreo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OreoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OreoApplication.class, args);
	}

}
