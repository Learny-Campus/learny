package com.example.authservice.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtRequest {
    private String username;
    private String email;
    private String password;
}
