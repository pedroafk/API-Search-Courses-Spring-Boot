package com.itextodesafio.desafioentrevista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.itextodesafio")
public class DesafioentrevistaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioentrevistaApplication.class, args);
	}

}
