package com.example.oreo.user.application;


import com.example.oreo.user.repository.UserRepository;

import jakarta.annotation.security.RolesAllowed;

import com.example.oreo.user.domain.Role;
import com.example.oreo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @RolesAllowed("CENTRAL")
    public List<User> all() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    @RolesAllowed("CENTRAL")
    public User one(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("CENTRAL")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
