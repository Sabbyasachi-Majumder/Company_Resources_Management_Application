package com.company.authenticate.testUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("admin1: " + encoder.encode("admin1"));
        System.out.println("disabledUser: " + encoder.encode("disabledUser"));
        System.out.println("user1: " + encoder.encode("user1"));
        System.out.println("admin2: " + encoder.encode("admin2"));
    }
}
