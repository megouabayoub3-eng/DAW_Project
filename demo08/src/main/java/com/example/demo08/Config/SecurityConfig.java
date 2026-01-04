package com.example.demo08.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/logout",
                                                                "/auth/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler((request, response, authentication) -> {
                                                        // Redirect based on role
                                                        if (authentication.getAuthorities().contains(
                                                                        new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                                                                response.sendRedirect(request.getContextPath()
                                                                                + "/admin/approvals");
                                                                return;
                                                        }
                                                        if (authentication.getAuthorities().contains(
                                                                        new SimpleGrantedAuthority("ROLE_USER"))) {
                                                                // Teachers and Students both have ROLE_USER by default;
                                                                // distinguish by username or additional role
                                                                // If the user is a teacher (has ROLE_USER but their
                                                                // username starts with "teacher" in seeded data),
                                                                // redirect to teacher dashboard. For students, use
                                                                // /check-status to route based on approval status.
                                                                String username = authentication.getName();
                                                                if (username != null && username
                                                                                .equalsIgnoreCase("teacher")) {
                                                                        response.sendRedirect(request.getContextPath()
                                                                                        + "/teacher/dashboard");
                                                                        return;
                                                                } else {
                                                                        response.sendRedirect(request.getContextPath()
                                                                                        + "/check-status");
                                                                        return;
                                                                }
                                                        }

                                                        // Fallback
                                                        response.sendRedirect(request.getContextPath() + "/");
                                                })
                                                .permitAll())
                                .logout(logout -> logout.permitAll());

                // Basic Content Security Policy to reduce XSS risk (adjust for external
                // resources)
                http.headers().contentSecurityPolicy("default-src 'self' 'unsafe-inline' https:;");

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public HttpFirewall httpFirewall() {
                StrictHttpFirewall firewall = new StrictHttpFirewall();
                firewall.setAllowSemicolon(true);
                return firewall;
        }
}