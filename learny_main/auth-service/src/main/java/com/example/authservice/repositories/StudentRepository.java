package com.example.authservice.repositories;

import com.example.authservice.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByUsername(String username);
    Student findStudentByIndex(Long index);
    void deleteStudentByIndex(Long index);
}
