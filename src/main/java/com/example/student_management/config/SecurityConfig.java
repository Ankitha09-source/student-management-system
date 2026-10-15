
package com.example.student_management.config;

import com.example.student_management.service.AppUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            AppUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider provider) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(provider)

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .authorizeHttpRequests(auth -> auth

                // Public HTML pages and static resources
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/style.css",
                    "/admin-access.html",
                    "/admin-login.html",
                    "/admin-register.html",
                    "/student-login.html",
                    "/student-register.html",
                    "/admin-dashboard.html",
                    "/student-dashboard.html",
                    "/favicon.ico"
                ).permitAll()

                // Login and registration endpoints
                .requestMatchers("/api/auth/**").permitAll()

                // Existing student login compatibility
                .requestMatchers(HttpMethod.GET, "/api/students").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/students/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/marks/**").permitAll()

                // Admin management operations
                .requestMatchers(
                    "/api/students/**",
                    "/api/courses/**",
                    "/api/marks/**"
                ).hasRole("ADMIN")

                // Role-specific API endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/student/**").hasRole("STUDENT")

                // All other requests require authentication
                .anyRequest().authenticated()
            )

            // Logout clears the session and authentication
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler((request, response, authentication) ->
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT)
                )
            )

            // Return HTTP errors for unauthorized requests
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, error) ->
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((request, response, error) ->
                    response.sendError(
                        HttpServletResponse.SC_FORBIDDEN))
            );

        return http.build();
            }
        }