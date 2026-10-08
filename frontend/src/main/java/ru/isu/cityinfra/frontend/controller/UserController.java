package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.isu.cityinfra.frontend.client.AuthClient;
import ru.isu.cityinfra.frontend.security.Sessions;

@Controller
public class UserController {

    private final AuthClient authClient;

    public UserController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        model.addAttribute("user", authClient.me(Sessions.token(session)));
        return "profile";
    }

    @GetMapping("/users")
    public String users(HttpSession session, Model model) {
        // права проверяет Auth Service: для обычного пользователя он вернет 403
        model.addAttribute("users", authClient.users(Sessions.token(session)));
        return "users";
    }
}
