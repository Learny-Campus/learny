package com.example.authservice.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TeacherRegistrationDto {
    private String username;
    private String secondName;
    private String password;
    private String confirmPassword;
    private String email;
    private String degree;
    private int index;
}
