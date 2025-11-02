package com.example.oreo.authentication.application;

import com.example.oreo.authentication.domain.AuthenticationService;
import com.example.oreo.authentication.dto.JwtAuthLoginDto;
import com.example.oreo.authentication.dto.LoginResponseDto;
import com.example.oreo.user.domain.UserService;
import com.example.oreo.user.dto.RegisterUserDto;
import com.example.oreo.user.dto.UserDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody final RegisterUserDto dto) {
        UserDto temp = authenticationService.jwtRegister(dto);
        return ResponseEntity.created(URI.create("/auth/user" + temp.getUserId())).body(temp);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody final JwtAuthLoginDto dto) {
        return ResponseEntity.ok(authenticationService.jwtLogin(dto));
    }

}
