package com.smartcity.controller;

import com.smartcity.dto.*;
import com.smartcity.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        log.info("Registration attempt - Name: {}, Email: {}, Phone: {}",
                req.getName(), req.getEmail(), req.getPhone());
        var response = authService.register(req);
        log.info("Registration successful for email: {}", req.getEmail());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        log.info("Login attempt for user: {}", req.getEmail());

        var response = authService.login(req);

        log.info("Login success for user: {}", req.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(Authentication auth) {
        log.info("Profile requested by: {}", auth.getName());
        return ResponseEntity.ok(authService.getProfile(auth.getName()));
    }
}
