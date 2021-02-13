package com.example.codecool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DaoHomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaoHomeworkApplication.class, args);
	}

}
