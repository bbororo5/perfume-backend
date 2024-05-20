package com.bside405.perfume.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PerfumeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerfumeProjectApplication.class, args);
	}

}
