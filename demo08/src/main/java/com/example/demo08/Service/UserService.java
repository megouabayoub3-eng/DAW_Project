package com.example.demo08.Service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo08.dto.UserDto;
import com.example.demo08.Model.Role;

public interface UserService {
    Page<UserDto> listUsers(Pageable pageable);

    UserDto getUser(Long id);

    UserDto createUser(UserDto dto, String rawPassword);

    UserDto updateUser(Long id, UserDto dto);

    UserDto setRoles(Long id, Set<Role> roles);

    UserDto setEnabled(Long id, boolean enabled);

    void deleteUser(Long id);
}