package com.example.oreo.authentication.domain;

import com.example.oreo.authentication.dto.JwtAuthLoginDto;
import com.example.oreo.authentication.dto.LoginResponseDto;
import com.example.oreo.exception.UsernameException;
import com.example.oreo.jwt.domain.JwtService;
import com.example.oreo.user.domain.User;
import com.example.oreo.user.domain.UserService;
import com.example.oreo.user.dto.RegisterUserDto;
import com.example.oreo.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto jwtRegister(final RegisterUserDto dto) {
        final UserDto createdUser = userService.registerUser(dto, passwordEncoder);
        return createdUser;
    }

    public LoginResponseDto jwtLogin(final JwtAuthLoginDto dto) {
        final User user = userService.findByUsername(dto.getUsername());
        if (user == null) throw new UsernameNotFoundException("User was not found.");

        if (user.getPassword() == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new UsernameException("Invalid password.");

        final String token = jwtService.generateToken(modelMapper.map(user, UserDetails.class));
        return new LoginResponseDto(
                token,
                Long.parseLong(System.getenv("JWT_EXP")),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
