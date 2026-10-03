package ru.isu.cityinfra.notification.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.notification.dto.NotificationRequestDto;
import ru.isu.cityinfra.notification.dto.NotificationResponseDto;
import ru.isu.cityinfra.notification.enums.NotificationChannel;
import ru.isu.cityinfra.notification.enums.NotificationStatus;
import ru.isu.cityinfra.notification.model.Notification;
import ru.isu.cityinfra.notification.repository.NotificationRepository;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public NotificationResponseDto create(NotificationRequestDto request) {
        NotificationChannel channel;
        try {
            channel = request.getChannel() != null
                    ? NotificationChannel.valueOf(request.getChannel())
                    : NotificationChannel.IN_APP;
        } catch (IllegalArgumentException e) {
            channel = NotificationChannel.IN_APP;
        }

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .channel(channel)
                .title(request.getTitle())
                .message(request.getMessage())
                .status(NotificationStatus.SENT)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Уведомление отправлено пользователю {}", saved.getUserId());
        return toDto(saved);
    }

    private NotificationResponseDto toDto(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .channel(n.getChannel())
                .nameChannel(n.getChannel().getDisplayName())
                .title(n.getTitle())
                .message(n.getMessage())
                .status(n.getStatus())
                .nameStatus(n.getStatus().getDisplayName())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
