package com.example.oreo.user.service;


import com.example.oreo.user.domain.User;
import com.example.oreo.user.domain.Role;
import com.example.oreo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String email, String passwordHash, Role role, String branch) {
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario o email ya existen");
        }

        if (role == Role.BRANCH && (branch == null || branch.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'branch' es obligatorio para role BRANCH");
        }

        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(passwordHash);
        u.setRole(role);
        u.setBranch(branch);
        u.setCreatedAt(Instant.now());
        return userRepository.save(u);
    }

    public List<User> listAll() {
        var currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.CENTRAL) {
            throw new AccessDeniedException("Solo CENTRAL puede ver la lista completa de usuarios");
        }
        return userRepository.findAll();
    }

    public User getById(String id) {
        var currentUser = getCurrentUser();
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (currentUser.getRole() == Role.BRANCH && !currentUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver otros usuarios");
        }
        return user;
    }

    public void delete(String id) {
        var currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.CENTRAL) {
            throw new AccessDeniedException("Solo CENTRAL puede eliminar usuarios");
        }

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        userRepository.deleteById(id);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }
}
