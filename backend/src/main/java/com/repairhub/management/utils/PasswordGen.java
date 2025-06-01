package com.repairhub.management.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGen {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }
}
