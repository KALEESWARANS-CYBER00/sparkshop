package com.sparkshop.service;

import com.sparkshop.dto.AuthResponse;
import com.sparkshop.dto.LoginRequest;
import com.sparkshop.dto.RegisterRequest;
import com.sparkshop.model.User;
import com.sparkshop.repository.UserRepository;
import com.sparkshop.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(List.of("USER"))
                .build();
        User saved = userRepository.save(u);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRoles());
        return new AuthResponse(token, "Bearer", Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "email", saved.getEmail(),
                "roles", saved.getRoles()
        ));
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getEmail(), u.getRoles());
        return new AuthResponse(token, "Bearer", Map.of(
                "id", u.getId(),
                "name", u.getName(),
                "email", u.getEmail(),
                "roles", u.getRoles()
        ));
    }
}
