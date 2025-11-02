package com.example.oreo.user.domain;

import com.example.oreo.exception.UsernameException;
import com.example.oreo.user.dto.RegisterUserDto;
import com.example.oreo.user.dto.UserDto;
import com.example.oreo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // must match encoded password in DB
                .roles(user.getRole().toString())         // ensure role is a String, e.g. "USER"
                .build();
    }

    public User findByUsername (String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserDto registerUser (RegisterUserDto dto, PasswordEncoder passwordEncoder) {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new UsernameException("Username already exists");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // HASH HERE
        user.setRole(dto.getRole());
        user.setBranch(dto.getBranch());

        return modelMapper.map( userRepository.saveAndFlush(user), UserDto.class); // change to return user
    }
}
