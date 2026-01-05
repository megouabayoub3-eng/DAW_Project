package com.example.demo08.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo08.Model.Document;
import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Model.ScholarshipApplication.Status;
import com.example.demo08.Model.Student;
import com.example.demo08.Model.User;
import com.example.demo08.Repository.DocumentRepository;
import com.example.demo08.Repository.ScholarshipApplicationRepository;
import com.example.demo08.Repository.StudentRepository;
import com.example.demo08.Repository.UserRepository;

@Service
@Transactional
public class ScholarshipService {

    private final ScholarshipApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ScholarshipService(ScholarshipApplicationRepository applicationRepository,
            StudentRepository studentRepository, DocumentRepository documentRepository, UserRepository userRepository,
            EmailService emailService) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public ScholarshipApplication applyForScholarship(String studentUsername, double amountRequested, String reason,
            List<MultipartFile> files) {
        Student student = studentRepository.findByUsername(studentUsername);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentUsername);
        }
        ScholarshipApplication app = new ScholarshipApplication(student, reason, amountRequested);
        ScholarshipApplication savedApp = applicationRepository.save(app);

        // Save documents if provided
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        Document document = new Document(
                                file.getOriginalFilename(),
                                getFileType(file.getOriginalFilename()),
                                file.getBytes(),
                                file.getSize(),
                                file.getContentType());
                        document.setScholarshipApplication(savedApp);
                        documentRepository.save(document);
                        savedApp.addDocument(document);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to save document: " + e.getMessage(), e);
                    }
                }
            }
        }

        return savedApp;
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

        // Send email notification (lookup user's email via username)
        String studentEmail = null;
        if (app.getStudent() != null) {
            String username = app.getStudent().getUsername();
            if (username != null) {
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null)
                    studentEmail = user.getEmail();
            }
        }
        if (studentEmail != null) {
            emailService.sendApplicationStatusUpdate(studentEmail, "APPROVED",
                    "Your scholarship application has been approved.");
        }

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

        // Send email notification (lookup user's email via username)
        String studentEmail2 = null;
        if (app.getStudent() != null) {
            String username = app.getStudent().getUsername();
            if (username != null) {
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null)
                    studentEmail2 = user.getEmail();
            }
        }
        if (studentEmail2 != null) {
            emailService.sendApplicationStatusUpdate(studentEmail2, "REJECTED", reason);
        }

        return true;
    }

    private String getFileType(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "UNKNOWN";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
    }
}
