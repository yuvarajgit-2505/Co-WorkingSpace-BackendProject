package com.flexidesk.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        response.put("email", authentication.getName());

        response.put(
                "roles",
                authentication.getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .toList()
        );

        return response;
    }


}
