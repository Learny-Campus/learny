package com.example.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String username;
    private String email;
    private int yearOfStudying;
    private String role;
}
