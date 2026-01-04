package com.example.demo08.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Model.ScholarshipApplication.Status;
import com.example.demo08.Model.Student;
import com.example.demo08.Repository.ScholarshipApplicationRepository;
import com.example.demo08.Repository.StudentRepository;

@Service
@Transactional
public class ScholarshipService {

    private final ScholarshipApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;

    public ScholarshipService(ScholarshipApplicationRepository applicationRepository,
            StudentRepository studentRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
    }

    public ScholarshipApplication applyForScholarship(String studentUsername, double amountRequested, String reason) {
        Student student = studentRepository.findByUsername(studentUsername);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentUsername);
        }
        ScholarshipApplication app = new ScholarshipApplication(student, reason, amountRequested);
        return applicationRepository.save(app);
    }

    public List<ScholarshipApplication> getPendingApplications() {
        return applicationRepository.findByStatus(Status.PENDING);
    }

    public List<ScholarshipApplication> getApplicationsByStudent(String username) {
        return applicationRepository.findByStudent_Username(username);
    }

    public boolean approveApplication(Long id, String teacherUsername) {
        Optional<ScholarshipApplication> maybe = applicationRepository.findById(id);
        if (maybe.isEmpty())
            return false;
        ScholarshipApplication app = maybe.get();
        if (app.getStatus() != Status.PENDING)
            return false;
        app.setStatus(Status.APPROVED);
        app.setReviewedAt(LocalDateTime.now());
        app.setReviewedBy(teacherUsername);
        applicationRepository.save(app);
        return true;
    }

    public boolean rejectApplication(Long id, String teacherUsername, String reason) {
        Optional<ScholarshipApplication> maybe = applicationRepository.findById(id);
        if (maybe.isEmpty())
            return false;
        ScholarshipApplication app = maybe.get();
        if (app.getStatus() != Status.PENDING)
            return false;
        app.setStatus(Status.REJECTED);
        app.setReviewedAt(LocalDateTime.now());
        app.setReviewedBy(teacherUsername);
        app.setRejectionReason(reason);
        applicationRepository.save(app);
        return true;
    }
}
