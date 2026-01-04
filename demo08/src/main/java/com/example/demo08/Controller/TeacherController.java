package com.example.demo08.Controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Model.Student;
import com.example.demo08.Service.ScholarshipService;
import com.example.demo08.Service.StudentService;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final StudentService studentService;

    private final ScholarshipService scholarshipService;

    public TeacherController(StudentService studentService, ScholarshipService scholarshipService) {
        this.studentService = studentService;
        this.scholarshipService = scholarshipService;
    }

    @GetMapping("/dashboard")
    public String teacherDashboard(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "teacher";
    }

    @PostMapping("/approve/{username}")
    public String approveStudent(@PathVariable String username, Authentication authentication) {
        String teacherUsername = authentication.getName();
        studentService.approveStudent(username, teacherUsername);
        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/reject/{username}")
    public String rejectStudent(@PathVariable String username,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        String teacherUsername = authentication.getName();
        studentService.rejectStudent(username, teacherUsername, reason);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/pending")
    public String showPendingStudents(Model model) {
        List<Student> pendingStudents = studentService.getPendingStudents();
        model.addAttribute("students", pendingStudents);
        return "pending";
    }

    @GetMapping("/scholarships")
    public String pendingScholarships(Model model) {
        List<ScholarshipApplication> apps = scholarshipService.getPendingApplications();
        model.addAttribute("applications", apps);
        return "teacher_scholarships";
    }

    @PostMapping("/scholarship/approve/{id}")
    public String approveScholarship(@PathVariable Long id, Authentication authentication) {
        scholarshipService.approveApplication(id, authentication.getName());
        return "redirect:/teacher/scholarships";
    }

    @PostMapping("/scholarship/reject/{id}")
    public String rejectScholarship(@PathVariable Long id,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        scholarshipService.rejectApplication(id, authentication.getName(), reason);
        return "redirect:/teacher/scholarships";
    }
}