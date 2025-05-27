package com.java.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*@SpringBootApplication(scanBasePackages = "com.java.employee")
@EnableJpaRepositories("com.java.employee.repository.*")
@ComponentScan(basePackages = { "com.java.employee.repository.*"})
@EntityScan("com.java.employee.repository.*")*/
@SpringBootApplication
@ComponentScan
public class EmployeeInformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeInformationApplication.class, args);
	}

}
