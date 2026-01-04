package com.example.demo08.Controller;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo08.Model.RegistrationStatus;
import com.example.demo08.Model.Role;
import com.example.demo08.Service.CustomUserDetailsService;
import com.example.demo08.Service.StudentService;
import com.example.demo08.Service.UserService;
import com.example.demo08.dto.SignupForm;
import com.example.demo08.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class RegistrationController {

    private final UserService userService;
    private final StudentService studentService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public RegistrationController(UserService userService, StudentService studentService,
            CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.studentService = studentService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @ModelAttribute("signupForm")
    public SignupForm signupFormModel() {
        return new SignupForm();
    }

    @PostMapping("/signup")
    public String handleSignup(@Valid @ModelAttribute("signupForm") SignupForm form,
            BindingResult binding,
            HttpServletRequest request,
            Model model) {

        if (binding.hasErrors()) {
            return "signup";
        }

        // use email as username to ensure uniqueness
        String username = form.getEmail();

        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setFullName(form.getName());
        dto.setEmail(form.getEmail());
        if ("teacher".equalsIgnoreCase(form.getAccountType())) {
            dto.setRoles(EnumSet.of(Role.ROLE_TEACHER));
        } else {
            dto.setRoles(EnumSet.of(Role.ROLE_USER));
        }
        dto.setEnabled(true);

        try {
            UserDto created = userService.createUser(dto, form.getPassword());
            if (!"teacher".equalsIgnoreCase(form.getAccountType())) {
                studentService.createStudent(username);
            }

            // If the newly created account is pending approval, show pending page instead
            // of auto-login
            if (created.getStatus() == RegistrationStatus.PENDING) {
                model.addAttribute("message", "Registration received. Your account is awaiting approval.");
                return "pending";
            }

            // Auto-login the user
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            boolean isTeacher = ud.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
            if (isTeacher) {
                return "redirect:/teacher/dashboard";
            }
            return "redirect:/student/dashboard";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "signup";
        }
    }
}
