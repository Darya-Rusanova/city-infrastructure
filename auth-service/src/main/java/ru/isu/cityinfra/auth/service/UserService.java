package ru.isu.cityinfra.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.auth.dto.UserPrivateDto;
import ru.isu.cityinfra.auth.dto.UserPublicDto;
import ru.isu.cityinfra.auth.exception.NotFoundException;
import ru.isu.cityinfra.auth.model.User;
import ru.isu.cityinfra.auth.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPrivateDto getMe(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь с id=" + id + " не найден"));
        return AuthService.toPrivateDto(user);
    }

    public UserPublicDto getPublic(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь с id=" + id + " не найден"));
        return UserPublicDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }

    public List<UserPublicDto> getAll() {
        return userRepository.findAll().stream()
                .map(u -> UserPublicDto.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .fullName(u.getFullName())
                        .build())
                .collect(Collectors.toList());
    }
}