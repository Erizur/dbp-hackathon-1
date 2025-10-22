package com.example.oreo.user.application;


import com.example.oreo.user.repository.UserRepository;
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
    public List<User> all() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable String id) {
        return userRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userRepository.deleteById(id);
    }
}
