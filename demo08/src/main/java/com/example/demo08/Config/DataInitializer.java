package com.example.demo08.Config;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo08.Model.RegistrationStatus;
import com.example.demo08.Model.Role;
import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Model.Student;
import com.example.demo08.Model.User;
import com.example.demo08.Repository.ScholarshipApplicationRepository;
import com.example.demo08.Repository.StudentRepository;
import com.example.demo08.Repository.UserRepository;

/**
 * Initializes the database with default users and students.
 * Ensures proper password encoding and prevents duplicate entries.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ScholarshipApplicationRepository scholarshipRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, StudentRepository studentRepository,
            ScholarshipApplicationRepository scholarshipRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeStudents();
    }

    /**
     * Creates default users with proper password encoding.
     * Checks for existing users to prevent duplicates on application restart.
     */
    private void initializeUsers() {
        List<String> usernames = Arrays.asList("admin", "teacher", "student");

        for (String username : usernames) {
            if (!userRepository.existsByUsername(username)) {
                User user = new User(username, username + "@example.com", passwordEncoder.encode(username + "123"));

                if ("admin".equals(username)) {
                    user.setRoles(EnumSet.of(Role.ROLE_ADMIN));
                } else {
                    user.setRoles(EnumSet.of(Role.ROLE_USER));
                }

                user.setStatus(RegistrationStatus.APPROVED);
                userRepository.save(user);
                System.out.println("Created user: " + username);
            } else {
                System.out.println("User already exists: " + username);
            }
        }
    }

    /**
     * Creates default student records with appropriate approval status.
     * Links to corresponding user accounts.
     */
    private void initializeStudents() {
        String[] studentUsernames = { "student" }; // Only the default student user

        for (String username : studentUsernames) {
            if (studentRepository.findByUsername(username) == null) {
                Student student = new Student(username);

                // Set accepted status for the default student
                student.setStatus(Student.ApprovalStatus.ACCEPTED);
                student.setApprovedByTeacher("teacher");

                studentRepository.save(student);
                System.out.println("Created student record: " + username + " with ACCEPTED status");
            }
        }
        // Seed a sample scholarship application for demo purposes
        Student demoStudent = studentRepository.findByUsername("student");
        if (demoStudent != null && scholarshipRepository.count() == 0) {
            ScholarshipApplication app = new ScholarshipApplication(demoStudent, "Need funding for research", 1500.00);
            scholarshipRepository.save(app);
            System.out.println("Created sample scholarship application for student: " + demoStudent.getUsername());
        }
    }
}