package com.example.demo08.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo08.Model.User;
import com.example.demo08.Repository.UserRepository;

/**
 * Custom implementation of UserDetailsService for loading user-specific data.
 * Integrates with our User entity and provides security context.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Check registration status
        if (user.getStatus() == RegistrationStatus.PENDING) {
            throw new DisabledException("Your account is waiting for Admin approval.");
        }
        if (user.getStatus() == RegistrationStatus.REJECTED) {
            throw new LockedException("Your account has been rejected.");
        }

        // Create GrantedAuthority from user role
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole()));

        // Return Spring Security's User object (not our custom User entity)
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // BCrypt encoded password
                authorities // Roles/permissions
        );
    }
}