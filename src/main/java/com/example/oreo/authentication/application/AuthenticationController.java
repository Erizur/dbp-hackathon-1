package com.example.oreo.authentication.application;

import com.example.oreo.authentication.domain.AuthenticationService;
import com.example.oreo.authentication.dto.JwtAuthLoginDto;
import com.example.oreo.authentication.dto.LoginResponseDto;
import com.example.oreo.user.domain.UserService;
import com.example.oreo.user.dto.RegisterUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public LoginResponseDto register(@Valid @RequestBody final RegisterUserDto dto) {
        return authenticationService.jwtRegister(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody final JwtAuthLoginDto dto) {
        return authenticationService.jwtLogin(dto);
    }

}
