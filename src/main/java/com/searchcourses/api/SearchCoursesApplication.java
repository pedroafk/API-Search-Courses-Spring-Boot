package com.searchcourses.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.searchcourses")
public class SearchCoursesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchCoursesApplication.class, args);
	}

}
