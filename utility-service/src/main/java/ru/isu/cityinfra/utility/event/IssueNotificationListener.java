package ru.isu.cityinfra.utility.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.isu.cityinfra.utility.client.NotificationClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssueNotificationListener {
    private NotificationClient notificationClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onIssueNotification(IssueNotificationEvent event) {
        log.info("Отправка уведомления после коммита: userId={}, title={}", event.userId(), event.title());
        try {
            notificationClient.send(event.userId(), event.title(), event.message());
        } catch (Exception e) {
            log.error("Ошибка отправки уведомления: {}", e.getMessage());
        }
    }
}