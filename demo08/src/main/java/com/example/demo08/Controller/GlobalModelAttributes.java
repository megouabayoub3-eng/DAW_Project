package com.example.demo08.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo08.Model.RegistrationStatus;
import com.example.demo08.Repository.UserRepository;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserRepository userRepository;

    @Autowired
    public GlobalModelAttributes(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addPendingCount(Model model) {
        try {
            long pending = userRepository.countByStatus(RegistrationStatus.PENDING);
            model.addAttribute("pendingCount", pending);
        } catch (Exception e) {
            model.addAttribute("pendingCount", 0);
        }
    }
}
