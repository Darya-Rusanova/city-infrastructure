package ru.isu.cityinfra.auth.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.auth.dto.*;
import ru.isu.cityinfra.auth.service.AuthService;

@Slf4j
@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserPrivateDto> register(
            @Valid @RequestBody RegisterRequestDto dto) {
        UserPrivateDto created = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}