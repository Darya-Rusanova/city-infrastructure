package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.isu.cityinfra.frontend.client.NotificationClient;
import ru.isu.cityinfra.frontend.security.Sessions;

@Controller
public class NotificationController {

    private final NotificationClient notifications;

    public NotificationController(NotificationClient notifications) {
        this.notifications = notifications;
    }

    @GetMapping("/notifications")
    public String list(HttpSession session, Model model) {
        model.addAttribute("notifications", notifications.getMy(Sessions.token(session)));
        return "notifications/list";
    }
}
