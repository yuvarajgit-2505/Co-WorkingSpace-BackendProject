package com.flexidesk.controller;

import com.flexidesk.dto.LoginRequestDTO;
import com.flexidesk.dto.RegisterRequestDTO;
import com.flexidesk.entity.User;
import com.flexidesk.repository.UserRepository;
import com.flexidesk.service.AuthService;
import com.flexidesk.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @SuppressWarnings("unused")
    private final AuthService authService;
   @PostMapping("/register")
    public ResponseEntity<?>register (@RequestBody RegisterRequestDTO dto){
        if(userRepository.existsByEmail(dto.getEmail())){
            return ResponseEntity.badRequest().body("Email already existed");
        }
        User user=User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(
                        dto.getRole() == null
                                ? "USER"
                                :  dto.getRole().toUpperCase()
                )
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User successfully registerd");
    }
    @PostMapping("/login")
public ResponseEntity<?>login(@RequestBody LoginRequestDTO dto){
    @SuppressWarnings("unused")
    Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    dto.getEmail(),dto.getPassword()));
    User user=userRepository.findByEmail(dto.getEmail()).orElseThrow();
    String token=jwtUtil.generateToken(user.getEmail(),user.getRole());

    Map<String, Object> response = new HashMap<>();
    response.put("token", token);
    response.put("email", user.getEmail());
    response.put("role", user.getRole());

    return ResponseEntity.ok(response);


}
}

