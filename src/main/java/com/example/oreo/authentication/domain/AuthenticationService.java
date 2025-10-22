package com.example.oreo.authentication.domain;

import com.example.oreo.authentication.dto.JwtAuthLoginDto;
import com.example.oreo.authentication.dto.LoginResponseDto;
import com.example.oreo.jwt.domain.JwtService;
import com.example.oreo.user.domain.User;
import com.example.oreo.user.domain.UserService;
import com.example.oreo.user.dto.RegisterUserDto;
import com.example.oreo.user.dto.UserDto;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto jwtRegister(final RegisterUserDto dto) {
        final UserDto createdUser = userService.registerUser(dto, passwordEncoder);
        // userService.sendVerificationEmail(createdUser);

        final String token = jwtService.generateToken(createdUser);
        return modelMapper.map(createdUser, LoginResponseDto.class);
        /*new LoginResponseDto(token,
                createdUser.getUserId(),
                createdUser.getEmail(),
                createdUser.getRole().name()
        );*/
    }

    public LoginResponseDto jwtLogin(final JwtAuthLoginDto dto) {
        final User user = userService.findByEmail(dto.getEmail());

        if (user.getPassword() == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            return null;

        final String token = jwtService.generateToken(modelMapper.map(user, UserDto.class));
        return new LoginResponseDto(
                token,
                user.getUserId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
