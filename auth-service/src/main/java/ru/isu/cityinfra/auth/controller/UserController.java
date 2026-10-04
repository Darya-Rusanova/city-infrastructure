package ru.isu.cityinfra.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.auth.dto.UserPrivateDto;
import ru.isu.cityinfra.auth.dto.UserPublicDto;
import ru.isu.cityinfra.auth.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserPrivateDto> me(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(userService.getMe(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getPublic(id));
    }

    @GetMapping
    public ResponseEntity<List<UserPublicDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}