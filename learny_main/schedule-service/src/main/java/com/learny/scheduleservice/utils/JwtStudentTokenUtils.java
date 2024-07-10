package com.learny.scheduleservice.utils;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Getter
public class JwtStudentTokenUtils {
    @Value("${jwt.student_secret}")
    private String studentSecret;

    @Value("${jwt.student_secret_lifetime}")
    private Duration studentSecretLifetime;

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

    public String getStudentUsername(String token) { return getAllClaimsFromStudentToken(token).getSubject(); }

    public List getRoles(String token) { return getAllClaimsFromStudentToken(token).get("roles", List.class); }
}