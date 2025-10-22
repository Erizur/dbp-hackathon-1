package com.example.oreo.user.repository;

import com.example.oreo.user.domain.User;  
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
}
