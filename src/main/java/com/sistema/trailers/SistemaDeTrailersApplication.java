package com.sistema.trailers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

@SpringBootApplication
public class SistemaDeTrailersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeTrailersApplication.class, args);
	}

    @Bean
    SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}
}
