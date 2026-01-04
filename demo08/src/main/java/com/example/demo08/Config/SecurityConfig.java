package com.example.demo08.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo08.Service.CustomUserDetailsService;

/**
 * Main security configuration using SecurityFilterChain (modern approach).
 * Configures authentication, authorization, and custom success handlers.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        public SecurityConfig(CustomUserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        /**
         * Configures the main security filter chain.
         * Defines URL patterns, access controls, and authentication mechanisms.
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.csrfTokenRepository(
                                                org.springframework.security.web.csrf.CookieCsrfTokenRepository
                                                                .withHttpOnlyFalse()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(1) // Limit concurrent sessions
                                                .maxSessionsPreventsLogin(false))
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**")
                                                .permitAll()
                                                .requestMatchers("/login").permitAll()
                                                .requestMatchers("/teacher/**").hasRole("TEACHER")
                                                .requestMatchers("/student/**").hasAnyRole("STUDENT")
                                                .requestMatchers("/pending").hasRole("STUDENT")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler(customSuccessHandler()) // Use role-based redirect
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());

                http.authenticationProvider(authenticationProvider());

                return http.build();
        }

        /**
         * Configures the authentication provider with custom user details service
         * and password encoder.
         */
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        /**
         * Bean for password encoder using BCrypt algorithm.
         * Essential for secure password storage.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Bean for authentication manager.
         * Required for programmatic authentication.
         */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
                        throws Exception {
                return config.getAuthenticationManager();
        }

        /**
         * Custom success handler that redirects based on user role and student status.
         * Handles the post-authentication routing logic.
         */
        @Bean
        public AuthenticationSuccessHandler customSuccessHandler() {
                return (request, response, authentication) -> {
                        String username = authentication.getName();
                        String role = authentication.getAuthorities().iterator().next().getAuthority();

                        if ("ROLE_TEACHER".equals(role)) {
                                response.sendRedirect("/teacher/dashboard");
                        } else if ("ROLE_STUDENT".equals(role)) {
                                // For students, check their approval status and redirect accordingly
                                response.sendRedirect("/check-status");
                        } else {
                                response.sendRedirect("/");
                        }
                };
        }
}