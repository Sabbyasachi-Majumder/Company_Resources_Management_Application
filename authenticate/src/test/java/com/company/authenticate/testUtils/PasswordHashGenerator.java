package com.company.authenticate.testUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("admin1: " + encoder.encode("admin1"));
        System.out.println("validPassword: " + encoder.encode("validPassword"));
    }
}
