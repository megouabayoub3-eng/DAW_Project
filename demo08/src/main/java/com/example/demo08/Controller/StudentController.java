package com.example.demo08.Controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo08.Model.ScholarshipApplication;
import com.example.demo08.Model.ScholarshipForm;
import com.example.demo08.Service.ScholarshipService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final ScholarshipService scholarshipService;

    public StudentController(ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    @GetMapping("/scholarships")
    public String catalog(Model model) {
        return "student_scholarships";
    }

    @GetMapping("/scholarship/apply")
    public String scholarshipApplyForm(Model model) {
        model.addAttribute("scholarshipForm", new ScholarshipForm());
        return "student_application_form";
    }

    @PostMapping("/scholarship/apply")
    public String submitScholarshipApplication(
            @Valid @ModelAttribute("scholarshipForm") ScholarshipForm scholarshipForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "student_application_form";
        }

        String username = authentication.getName();
        scholarshipService.applyForScholarship(username, scholarshipForm.getAmount(), scholarshipForm.getReason());
        return "redirect:/student/scholarship/my-applications";
    }

    @GetMapping("/scholarship/my-applications")
    public String myScholarshipApplications(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<ScholarshipApplication> apps = scholarshipService.getApplicationsByStudent(username);
        model.addAttribute("applications", apps);
        return "student_applications";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "student_profile";
    }

    @GetMapping("/dashboard")
    public String studentDashboard(Model model) {
        model.addAttribute("message", "Welcome to your student dashboard!");
        return "student";
    }

    @GetMapping("/pending")
    public String pendingPage(Model model) {
        model.addAttribute("message", "Your registration is currently under review. Please wait for approval.");
        return "pending";
    }

    @GetMapping("/rejected")
    public String rejectedPage(Model model) {
        model.addAttribute("message", "Your registration has been rejected. Please contact an administrator.");
        return "rejected";
    }
}
