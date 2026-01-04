package com.example.demo08.Service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo08.Model.Role;
import com.example.demo08.Model.User;
import com.example.demo08.Repository.UserRepository;
import com.example.demo08.Service.UserService;
import com.example.demo08.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getRoles(), u.isEnabled(),
                u.getStatus());
    }

    @Override
    public Page<UserDto> listUsers(Pageable pageable) {
        return repo.findAll(pageable).map(this::toDto);
    }

    @Override
    public UserDto getUser(Long id) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toDto(u);
    }

    @Override
    public UserDto createUser(UserDto dto, String rawPassword) {
        // enforce uniqueness of username/email
        if (dto.getUsername() != null && repo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }
        if (dto.getEmail() != null && repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles(dto.getRoles());
        u.setEnabled(dto.isEnabled());
        return toDto(repo.save(u));
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setUsername(dto.getUsername());
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setRoles(dto.getRoles());
        u.setEnabled(dto.isEnabled());
        return toDto(repo.save(u));
    }

    @Override
    public UserDto setRoles(Long id, Set<Role> roles) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setRoles(roles);
        return toDto(repo.save(u));
    }

    @Override
    public UserDto setEnabled(Long id, boolean enabled) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setEnabled(enabled);
        return toDto(repo.save(u));
    }

    @Override
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}