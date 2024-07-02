package com.example.authservice.repositories;

import com.example.authservice.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findTeacherByIndex(Long index);
    Optional<Teacher> findTeacherByUsername(String username);
    void deleteTeacherByIndex(Long index);
}
