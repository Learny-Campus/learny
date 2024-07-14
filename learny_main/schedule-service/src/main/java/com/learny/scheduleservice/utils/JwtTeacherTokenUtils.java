package com.learny.scheduleservice.utils;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Getter
@Component
public class JwtTeacherTokenUtils {
    @Value("${jwt.teacher_secret}")
    private String teacherSecret;

    @Value("${jwt.teacher_secret_lifetime}")
    private Duration teacherSecretLifetime;


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

    public String getTeacherUsername(String token) { return getAllClaimsFromTeacherToken(token).getSubject(); }

    public List getRoles(String token) { return getAllClaimsFromTeacherToken(token).get("roles", List.class); }
}
