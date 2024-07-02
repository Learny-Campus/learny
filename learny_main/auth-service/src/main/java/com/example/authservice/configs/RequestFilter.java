package com.example.authservice.configs;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.authservice.utils.JwtTeacherTokenUtils;
import com.example.authservice.utils.JwtStudentTokenUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestFilter extends OncePerRequestFilter {
    private final JwtTeacherTokenUtils jwtTeacherTokenUtils;
    private final JwtStudentTokenUtils jwtStudentTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                if (jwtTeacherTokenUtils.isValidToken(jwt)) {
                    username = jwtTeacherTokenUtils.getTeacherUsername(jwt);
                } else if (jwtStudentTokenUtils.isValidToken(jwt)) {
                    username = jwtStudentTokenUtils.getStudentUsername(jwt);
                }
            } catch (ExpiredJwtException ex) {
                log.debug("JWT token expired");
            } catch (SignatureException ex) {
                log.debug("JWT token signature exception");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (jwtTeacherTokenUtils.isValidToken(jwt)) {
                authorities.addAll(jwtTeacherTokenUtils.getRoles(jwt).stream()
                        .map(role -> new SimpleGrantedAuthority((String) role))
                        .toList());
            } else if (jwtStudentTokenUtils.isValidToken(jwt)) {
                authorities.addAll(jwtStudentTokenUtils.getRoles(jwt).stream()
                        .map(role -> new SimpleGrantedAuthority((String) role))
                        .toList());
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
