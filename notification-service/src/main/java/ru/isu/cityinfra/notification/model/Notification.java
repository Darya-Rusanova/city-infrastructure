package ru.isu.cityinfra.notification.model;

import jakarta.persistence.*;
import lombok.*;
import ru.isu.cityinfra.notification.enums.NotificationChannel;
import ru.isu.cityinfra.notification.enums.NotificationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_user_id", columnList = "user_id"),
        @Index(name = "idx_notifications_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel = NotificationChannel.IN_APP;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.SENT;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
