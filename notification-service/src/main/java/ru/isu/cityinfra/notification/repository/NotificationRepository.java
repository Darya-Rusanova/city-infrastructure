package ru.isu.cityinfra.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.cityinfra.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
