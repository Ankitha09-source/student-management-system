
package com.example.student_management.service;

import com.example.student_management.entity.AppUser;
import com.example.student_management.repository.AppUserRepository;
import com.example.student_management.repository.StudentRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AppUserRepository appUserRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Existing registration method for general account creation.
    public AppUser register(String email, String password, String role) {
        String normalizedEmail = normalizeEmail(email);

        if (appUserRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        AppUser user = new AppUser();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return appUserRepository.save(user);
    }

    // Register a student only if their email exists in student records.
    @Transactional
    public AppUser registerStudent(String email, String password) {
        String normalizedEmail = normalizeEmail(email);

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }

        boolean studentExists =
                studentRepository.existsByEmailIgnoreCase(normalizedEmail);

        if (!studentExists) {
            throw new IllegalArgumentException(
                    "No student record exists for this email. Contact your administrator.");
        }

        if (appUserRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "An account already exists for this email");
        }

        AppUser user = new AppUser();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("STUDENT");

        return appUserRepository.save(user);
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required");
        }

        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
