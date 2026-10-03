package ru.isu.cityinfra.notification.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.notification.dto.NotificationRequestDto;
import ru.isu.cityinfra.notification.dto.NotificationResponseDto;
import ru.isu.cityinfra.notification.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDto> create(@Valid @RequestBody NotificationRequestDto request) {
        NotificationResponseDto response = notificationService.create(request);
        return ResponseEntity.status(201).body(response);
    }
}
