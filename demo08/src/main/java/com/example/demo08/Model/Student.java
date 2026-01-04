package com.example.demo08.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Student entity containing approval status and related metadata.
 * Tracks student registration and approval workflow.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Links to User entity

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING; // Default to PENDING

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "approved_by_teacher")
    private String approvedByTeacher;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    // Enums for approval status
    public enum ApprovalStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    // Constructors
    public Student() {
    }

    public Student(String username) {
        this.username = username;
        this.registrationDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovedByTeacher() {
        return approvedByTeacher;
    }

    public void setApprovedByTeacher(String approvedByTeacher) {
        this.approvedByTeacher = approvedByTeacher;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}