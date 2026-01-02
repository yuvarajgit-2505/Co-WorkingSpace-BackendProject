package com.flexidesk.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SecretKey="THIS_IS_A_VERY_LONG_SECRET_KEY_FOR_JWT_FLEXIDESK_123456";
    private static final long ExpirationTime=1000*60*60;
     private Key getSigningKey(){
         return Keys.hmacShaKeyFor(SecretKey.getBytes());
     }
     public String generateToken(String email, String role){
         return Jwts.builder()
                 .setSubject(email)
                 .claim("role",role)
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(System.currentTimeMillis()+ExpirationTime))
                 .signWith(getSigningKey())
                 .compact();
     }
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        return !getClaims(token).getExpiration().before(new Date());
    }
    private Claims getClaims(String token){
         return Jwts.parser()
                 .setSigningKey(getSigningKey())
                 .build()
                 .parseClaimsJws(token)
                 .getBody();
    }
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }


}
