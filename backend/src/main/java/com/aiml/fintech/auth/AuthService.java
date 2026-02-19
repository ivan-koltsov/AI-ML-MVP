package com.aiml.fintech.auth;

import com.aiml.fintech.auth.dto.SignInRequest;
import com.aiml.fintech.auth.dto.SignUpRequest;
import com.aiml.fintech.auth.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElse(null);
        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.info("auth event: sign-in failed, email={}", maskEmail(request.email()));
            throw new AuthException("Invalid email or password");
        }
        log.info("auth event: sign-in success, userId={}", user.getId());
        return toResponse(user);
    }

    public UserResponse signUp(SignUpRequest request) {
        if (!PASSWORD_PATTERN.matcher(request.password()).matches()) {
            throw new AuthBadRequestException("Password must be at least 8 characters with at least one letter and one digit");
        }
        if (userRepository.existsByEmail(request.email())) {
            log.info("auth event: sign-up rejected, email already exists");
            throw new AuthConflictException("Email already registered");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user = userRepository.save(user);
        log.info("auth event: sign-up success, userId={}", user.getId());
        return toResponse(user);
    }

    public void signOut() {
    }

    public User findUserById(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserResponse getCurrentUser(User user) {
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }

    private static String maskEmail(String email) {
        if (email == null || email.length() < 3) return "***";
        return email.charAt(0) + "***" + email.substring(email.length() - 1);
    }
}
