package com.example.demo08.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo08.Model.RegistrationStatus;
import com.example.demo08.Model.User;
import com.example.demo08.Repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/analytics")
    public String analytics(Model model) {
        return "analytics";
    }

    @GetMapping("/notifications")
    public String notifications(Model model) {
        return "notifications";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return "settings";
    }

    // 1. View all Pending Users
    @GetMapping("/approvals")
    public String viewPendingApprovals(Model model) {
        List<User> pendingUsers = userRepository.findByStatus(RegistrationStatus.PENDING);
        model.addAttribute("pendingUsers", pendingUsers);
        return "admin_approvals";
    }

    // 2. Approve a User
    @PostMapping("/approve/{id}")
    public String approveUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(RegistrationStatus.APPROVED);
            userRepository.save(user);
        }
        return "redirect:/admin/approvals";
    }

    // 3. Reject a User
    @PostMapping("/reject/{id}")
    public String rejectUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(RegistrationStatus.REJECTED);
            userRepository.save(user);
        }
        return "redirect:/admin/approvals";
    }
}
