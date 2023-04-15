package com.nikola.blog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGeneratorEncoder {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Encoded password for 'nikola': "+passwordEncoder.encode("nikola"));
        System.out.println("Encoded password for 'admin': "+passwordEncoder.encode("admin"));
        System.out.println("Encoded password for 'user': "+passwordEncoder.encode("user"));
    }
}
