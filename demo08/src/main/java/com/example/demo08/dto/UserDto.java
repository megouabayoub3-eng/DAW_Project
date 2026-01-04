package com.example.demo08.dto;

import java.util.Set;

import com.example.demo08.Model.RegistrationStatus;
import com.example.demo08.Model.Role;

public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Set<Role> roles;
    private boolean enabled;
    private RegistrationStatus status;

    public UserDto() {
    }

    public UserDto(Long id, String username, String email, Set<Role> roles, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
    }

    public UserDto(Long id, String username, String fullName, String email, Set<Role> roles, boolean enabled,
            RegistrationStatus status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
        this.status = status;
    }

    public UserDto(Long id, String username, String email, Set<Role> roles, boolean enabled,
            RegistrationStatus status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
        this.status = status;
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}