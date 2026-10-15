
package com.example.student_management.controller;

import com.example.student_management.entity.AppUser;
import com.example.student_management.repository.AppUserRepository;
import com.example.student_management.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            AuthService authService,
            AppUserRepository appUserRepository,
            AuthenticationManager authenticationManager) {

        this.authService = authService;
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
    }

    // Register a student only when their email exists in student records.
    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank()
                || password == null || password.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message",
                    "Enter a valid email and a password of at least 8 characters"));
        }

        try {
            AppUser user = authService.registerStudent(email, password);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Student registered successfully",
                    "email", user.getEmail(),
                    "role", user.getRole()));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()));
        }
    }

    // Admin registration.
    // IMPORTANT: Restrict this endpoint before deploying publicly.
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank()
                || password == null || password.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message",
                    "Enter a valid email and a password of at least 8 characters"));
        }

        try {
            AppUser user = authService.register(
                    email.trim().toLowerCase(java.util.Locale.ROOT),
                    password,
                    "ADMIN");

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Admin registered successfully",
                    "email", user.getEmail(),
                    "role", user.getRole()));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", ex.getMessage()));
        }
    }

    // Authenticate the user and save authentication in the HTTP session.
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email and password are required"));
        }

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    email.trim().toLowerCase(
                                            java.util.Locale.ROOT),
                                    password));

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            HttpSession session = httpRequest.getSession(true);

            session.setAttribute(
                    HttpSessionSecurityContextRepository
                            .SPRING_SECURITY_CONTEXT_KEY,
                    context);

            AppUser user = appUserRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Authenticated user record not found"));

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "email", user.getEmail(),
                    "role", user.getRole()));

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Invalid email or password"));
        }
    }

    // Logout and invalidate the current session.
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }
}
