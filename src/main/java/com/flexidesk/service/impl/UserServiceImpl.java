package com.flexidesk.service.impl;

import com.flexidesk.entity.User;
import com.flexidesk.repository.UserRepository;
import com.flexidesk.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder PasswordEncoder;
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.PasswordEncoder=passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("User already exists with this email");
        }
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
    }
}
