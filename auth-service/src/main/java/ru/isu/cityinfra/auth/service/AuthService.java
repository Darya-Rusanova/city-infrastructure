package ru.isu.cityinfra.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isu.cityinfra.auth.dto.*;
import ru.isu.cityinfra.auth.enums.Role;
import ru.isu.cityinfra.auth.exception.ConflictException;
import ru.isu.cityinfra.auth.exception.UnauthorizedException;
import ru.isu.cityinfra.auth.model.User;
import ru.isu.cityinfra.auth.repository.UserRepository;
import ru.isu.cityinfra.auth.security.JwtService;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserPrivateDto register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .role(Role.USER) // роль всегда USER, из запроса не принимаем
                .build();

        User saved = userRepository.save(user);
        log.info("Зарегистрирован новый пользователь id={}, username={}",
                saved.getId(), saved.getUsername());

        return toPrivateDto(saved);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UnauthorizedException(
                        "Неверное имя пользователя или пароль"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            log.warn("Неудачная попытка входа для username={}", dto.getUsername());
            throw new UnauthorizedException("Неверное имя пользователя или пароль");
        }

        String token = jwtService.generateToken(user);
        log.info("Успешный вход пользователя id={}, username={}",
                user.getId(), user.getUsername());

        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getTtlSeconds())
                .build();
    }

    public static UserPrivateDto toPrivateDto(User user) {
        return UserPrivateDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .nameRole(user.getRole().getDisplayName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}