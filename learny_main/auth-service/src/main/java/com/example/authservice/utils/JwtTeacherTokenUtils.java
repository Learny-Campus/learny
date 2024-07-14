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
public class JwtTeacherTokenUtils {
    @Value("${jwt.student_secret}")
    private String teacherSecret;

    @Value("${jwt.student_secret_lifetime}")
    private Duration teacherSecretLifetime;

    public String generationTeacherToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roleList);

        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + getTeacherSecretLifetime().toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, getTeacherSecret())
                .compact();
    }

    private Claims getAllClaimsFromTeacherToken(String token) {
        return Jwts.parser()
                .setSigningKey(getTeacherSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            getAllClaimsFromTeacherToken(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            return false;
        }
    }

    public String getTeacherUsername(String token) {
        return getAllClaimsFromTeacherToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromTeacherToken(token).get("roles", List.class);
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
