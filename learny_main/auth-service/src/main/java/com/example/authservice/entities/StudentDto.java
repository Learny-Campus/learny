package com.example.authservice.entities;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */
@Value
public class StudentDto implements Serializable {
    Long id;
}