package com.example.authservice.utils;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class JwtStudentTokenUtils {

    @Value("${jwt.student_secret}")
    private String studentSecret;

    @Value("${jwt.student_secret_lifetime}")
    private Duration studentSecretLifetime;

    public String generateStudentToken(UserDetails userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of(role));

        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + getStudentSecretLifetime().toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, getStudentSecret())
                .compact();
    }

    private Claims getAllClaimsFromStudentToken(String token) {
        return Jwts.parser()
                .setSigningKey(getStudentSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            getAllClaimsFromStudentToken(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            return false;
        }
    }

    public String getStudentUsername(String token) {
        return getAllClaimsFromStudentToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromStudentToken(token).get("roles", List.class);
    }

    public String getFirstRole(String token) {
        List<String> roles = getRoles(token);
        if (roles != null && !roles.isEmpty()) {
            return roles.getFirst();
        }
        return null; // или выбросить исключение, если роли нет
    }

    public boolean hasRole(String token, String role) {
        List<String> roles = getRoles(token);
        return roles != null && roles.contains(role);
    }
}
