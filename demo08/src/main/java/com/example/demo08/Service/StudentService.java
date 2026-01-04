package com.example.demo08.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo08.Model.Student;
import com.example.demo08.Repository.StudentRepository;

/**
 * Service layer for student approval workflow operations.
 * Handles approval, rejection, and status management with transaction support.
 */
@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves all students for display in teacher dashboard.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAllByOrderByRegistrationDateDesc();
    }

    /**
     * Retrieves all pending students for approval/rejection.
     */
    public List<Student> getPendingStudents() {
        return studentRepository.findByStatus(Student.ApprovalStatus.PENDING);
    }

    /**
     * Approves a student registration.
     * Updates status to ACCEPTED and records approval timestamp and teacher.
     */
    public boolean approveStudent(String username, String approvedByTeacher) {
        Student student = studentRepository.findByUsername(username);

        if (student != null && student.getStatus() == Student.ApprovalStatus.PENDING) {
            student.setStatus(Student.ApprovalStatus.ACCEPTED);
            student.setApprovalDate(LocalDateTime.now());
            student.setApprovedByTeacher(approvedByTeacher);

            studentRepository.save(student);
            return true;
        }

        return false; // Student not found or already processed
    }

    /**
     * Rejects a student registration.
     * Updates status to REJECTED and records rejection reason.
     */
    public boolean rejectStudent(String username, String rejectedByTeacher, String reason) {
        Student student = studentRepository.findByUsername(username);

        if (student != null && student.getStatus() == Student.ApprovalStatus.PENDING) {
            student.setStatus(Student.ApprovalStatus.REJECTED);
            student.setApprovalDate(LocalDateTime.now());
            student.setApprovedByTeacher(rejectedByTeacher);
            student.setRejectionReason(reason);

            studentRepository.save(student);
            return true;
        }

        return false; // Student not found or already processed
    }

    /**
     * Gets the approval status of a specific student.
     */
    public Student.ApprovalStatus getStudentStatus(String username) {
        Student student = studentRepository.findByUsername(username);
        return student != null ? student.getStatus() : Student.ApprovalStatus.REJECTED;
    }
}