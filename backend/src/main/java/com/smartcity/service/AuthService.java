package com.smartcity.service;

import com.smartcity.dto.*;
import com.smartcity.entity.User;
import com.smartcity.exception.CustomException;
import com.smartcity.repository.UserRepository;
import com.smartcity.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new CustomException("Email already registered");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .role(User.Role.CITIZEN)
                .status(User.Status.ACTIVE)
                .build();
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, "Registration successful");
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new CustomException("Invalid credentials");
        }
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, "Login successful");
    }

   /* public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));
    }*/

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .department(String.valueOf(user.getDepartmentId()))
                .role(user.getRole().name())
                .status(String.valueOf(user.getStatus()))
                .build();
    }

}
