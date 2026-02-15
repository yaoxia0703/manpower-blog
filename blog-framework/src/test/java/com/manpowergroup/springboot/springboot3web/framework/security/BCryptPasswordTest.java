package com.manpowergroup.springboot.springboot3web.framework.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void generate_and_match() {
        String raw = "12345678";
        String encoded = passwordEncoder.encode(raw);

        System.out.println("[BCrypt] raw     = " + raw);
        System.out.println("[BCrypt] encoded = " + encoded);

        assertTrue(passwordEncoder.matches(raw, encoded));
        assertFalse(passwordEncoder.matches("wrong", encoded));
    }
}
