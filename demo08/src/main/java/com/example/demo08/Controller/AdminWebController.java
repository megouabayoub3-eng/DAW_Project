package com.example.demo08.Controller;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo08.Model.Role;
import com.example.demo08.Service.UserService;
import com.example.demo08.dto.UserDto;

@Controller
@RequestMapping("/admin/users")
public class AdminWebController {
    private final UserService userService;
    private final com.example.demo08.Service.StudentService studentService;

    @Autowired
    public AdminWebController(UserService userService, com.example.demo08.Service.StudentService studentService) {
        this.userService = userService;
        this.studentService = studentService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("users", userService.listUsers(org.springframework.data.domain.PageRequest.of(page, 20)));
        return "users";
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "admin_students";
    }

    @PostMapping("/students/{username}/approve")
    public String approveStudentByAdmin(@PathVariable String username) {
        studentService.approveStudent(username, "admin");
        return "redirect:/admin/students";
    }

    @PostMapping("/students/{username}/reject")
    public String rejectStudentByAdmin(@PathVariable String username, @RequestParam(required = false) String reason) {
        studentService.rejectStudent(username, "admin", reason);
        return "redirect:/admin/students";
    }

    @GetMapping("/teachers")
    public String listTeachers(Model model) {
        // fetch a large page and filter by ROLE_TEACHER
        List<com.example.demo08.dto.UserDto> teachers = userService.listUsers(PageRequest.of(0, 1000))
                .stream()
                .filter(u -> u.getRoles() != null && u.getRoles().contains(com.example.demo08.Model.Role.ROLE_TEACHER))
                .collect(Collectors.toList());
        model.addAttribute("teachers", teachers);
        return "admin_teachers";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        UserDto u = userService.getUser(id);
        model.addAttribute("user", u);
        model.addAttribute("allRoles", EnumSet.allOf(Role.class));
        return "users-view";
    }

    @PostMapping("/{id}/roles")
    public String updateRoles(@PathVariable Long id, @RequestParam(required = false) Role[] roles) {
        userService.setRoles(id,
                roles == null ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(java.util.Arrays.asList(roles)));
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/{id}/enable")
    public String setEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        userService.setEnabled(id, enabled);
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}