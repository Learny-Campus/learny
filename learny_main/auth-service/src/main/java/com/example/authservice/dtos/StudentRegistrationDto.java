package com.example.authservice.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StudentRegistrationDto {
    private String username;
    private String secondName;
    private String password;
    private String confirmPassword;
    private String email;
    private int index;
    private int yearOfStudying;
}
