package com.flexidesk.config;
import com.flexidesk.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println("JwtAuthFilter: Incoming request path: " + path);


        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("JwtAuthFilter: Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                System.out.println("JwtAuthFilter: Extracted email: " + email);
                System.out.println("JwtAuthFilter: Extracted role: " + role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_"+role))
                        );

                System.out.println("Final injected authorities: " + authentication.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("JwtAuthFilter: Token invalid or expired");
            }
        }else {
            System.out.println("JwtAuthFilter: Authorization header missing or does not start with Bearer");
        }

        filterChain.doFilter(request, response);
    }
}
