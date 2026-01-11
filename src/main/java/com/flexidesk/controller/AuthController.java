package com.flexidesk.controller;

import com.flexidesk.dto.LoginRequestDTO;
import com.flexidesk.dto.RegisterRequestDTO;
import com.flexidesk.entity.User;
import com.flexidesk.repository.UserRepository;
import com.flexidesk.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already existed");
        }

       User user = User.builder()
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .role(
            dto.getRole() == null ? "USER" : dto.getRole())
        
        .build();

        userRepository.save(user);
        return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {

        String token = authService.login(dto.getEmail(), dto.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", dto.getEmail());

        return ResponseEntity.ok(response);
    }
}

