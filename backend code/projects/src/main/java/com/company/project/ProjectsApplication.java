package com.company.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ProjectsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectsApplication.class, args);
	}

}
