package com.java.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class EmployeeInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeInformationApplication.class, args);
    }

}
