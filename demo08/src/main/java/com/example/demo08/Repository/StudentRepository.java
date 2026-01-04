package com.example.demo08.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo08.Model.Student;

/**
 * Repository for Student entity operations.
 * Manages student approval workflow and status tracking.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find student by username.
     */
    Student findByUsername(String username);

    /**
     * Find all students with PENDING status.
     */
    List<Student> findByStatus(Student.ApprovalStatus status);

    /**
     * Find all students ordered by registration date.
     */
    List<Student> findAllByOrderByRegistrationDateDesc();
}