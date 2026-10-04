package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.frontend.security.JwtUtil;

/**
 * Временный контроллер логина. Identity & Auth Service ещё не реализует
 * настоящую регистрацию/аутентификацию, поэтому здесь форма только запрашивает
 * имя пользователя и роль и генерирует JWT локально (см. JwtUtil).
 * Когда auth-service будет готов, POST /login должен быть заменён на вызов
 * POST {services.auth.url}/auth/login и обработку настоящего токена из ответа.
 */
@Controller
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                         @RequestParam String role,
                         HttpSession session,
                         Model model) {
        if (username == null || username.isBlank()) {
            model.addAttribute("error", "Введите имя пользователя");
            return "login";
        }

        // Временно: стабильный числовой id из имени пользователя,
        // пока нет настоящей таблицы users в auth-service.
        String userId = String.valueOf(Math.abs(username.hashCode() % 100000));
        String token = jwtUtil.generateToken(userId, role);

        session.setAttribute("userId", userId);
        session.setAttribute("username", username);
        session.setAttribute("role", role);
        session.setAttribute("token", token);

        return "redirect:/issues";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
