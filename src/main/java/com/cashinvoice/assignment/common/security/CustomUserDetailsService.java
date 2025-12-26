package com.cashinvoice.assignment.common.security;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Example in-memory users
        if (username.equals("admin")) {
            return new org.springframework.security.core.userdetails.User(
                    "admin",
                    "$2a$10$Lc2YbhfKkJmyEpTBIdfVseaXniXSU7yVtzSg3ICuCr6cXnK1ffEzK", // password: admin123
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        } else if (username.equals("user")) {
            return new org.springframework.security.core.userdetails.User(
                    "user",
                    "$2a$10$ekaddaUUswkO4PHylz4PGen1VuLjmrxKkT0w3wJYq4t2SOKc9kPqG", // password: user123
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }
        throw new RuntimeException("User not found");
    }
}