package com.company.authenticate.testUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("admin0001: " + encoder.encode("admin0001"));
        System.out.println("disabledUser: " + encoder.encode("disabledUser"));
        System.out.println("user0001: " + encoder.encode("user0001"));
        System.out.println("admin0002: " + encoder.encode("admin0002"));
    }
}
