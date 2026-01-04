package com.example.demo08.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo08.Model.Student;
import com.example.demo08.Service.StudentService;

@Controller
public class HomeController {

    private final StudentService studentService;

    public HomeController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping({ "/", "/home" })
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/check-status")
    public String checkStudentStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student.ApprovalStatus status = studentService.getStudentStatus(username);

        switch (status) {
            case ACCEPTED:
                return "redirect:/student/dashboard";
            case PENDING:
                return "redirect:/pending";
            case REJECTED:
                return "redirect:/rejected";
            default:
                return "redirect:/";
        }
    }
}