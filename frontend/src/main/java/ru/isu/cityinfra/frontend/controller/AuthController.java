package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.isu.cityinfra.frontend.client.AuthClient;
import ru.isu.cityinfra.frontend.dto.AuthTokenDto;
import ru.isu.cityinfra.frontend.dto.LoginForm;
import ru.isu.cityinfra.frontend.dto.LoginRequestDto;
import ru.isu.cityinfra.frontend.dto.RegisterForm;
import ru.isu.cityinfra.frontend.dto.RegisterRequestDto;
import ru.isu.cityinfra.frontend.dto.UserDto;
import ru.isu.cityinfra.frontend.exception.ApiException;
import ru.isu.cityinfra.frontend.security.Sessions;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Вход, регистрация и выход. Токен выдает Auth Service, фронтенд только хранит его в сессии.
 */
@Controller
public class AuthController {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final AuthClient authClient;

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request, Model model) {
        if (Sessions.isLoggedIn(request.getSession(false))) {
            return "redirect:/dashboard";
        }
        model.addAttribute("form", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("form") LoginForm form, HttpServletRequest request, Model model) {
        String username = form.getUsername() == null ? "" : form.getUsername().trim();
        String password = form.getPassword();
        form.setUsername(username);
        form.setPassword(null);

        if (username.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Введите имя пользователя и пароль");
            return "login";
        }
        try {
            startSession(request, username, password);
            return "redirect:/dashboard";
        } catch (ApiException e) {
            model.addAttribute("error", e.getStatus() == 401
                    ? "Неверное имя пользователя или пароль"
                    : e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm(HttpServletRequest request, Model model) {
        if (Sessions.isLoggedIn(request.getSession(false))) {
            return "redirect:/dashboard";
        }
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegisterForm form, HttpServletRequest request, Model model) {
        String password = form.getPassword();
        String confirm = form.getConfirmPassword();
        form.setUsername(trim(form.getUsername()));
        form.setFullName(trim(form.getFullName()));
        form.setEmail(trim(form.getEmail()));
        form.setPassword(null);
        form.setConfirmPassword(null);

        String problem = validate(form, password, confirm);
        if (problem != null) {
            model.addAttribute("error", problem);
            return "register";
        }
        try {
            authClient.register(new RegisterRequestDto(
                    form.getUsername(), form.getEmail(), password,
                    form.getFullName().isEmpty() ? null : form.getFullName()));
        } catch (ApiException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        // аккаунт создан: сразу входим, чтобы не заставлять вводить те же данные второй раз
        try {
            startSession(request, form.getUsername(), password);
            return "redirect:/dashboard";
        } catch (ApiException e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }

    /** Логинится в Auth Service, загружает профиль и кладет данные в новую сессию. */
    private void startSession(HttpServletRequest request, String username, String password) {
        AuthTokenDto auth = authClient.login(new LoginRequestDto(username, password));
        if (auth == null || auth.getToken() == null || auth.getToken().isBlank()) {
            throw new ApiException(502, "Сервис входа вернул пустой ответ, попробуйте позже");
        }
        UserDto user = authClient.me(auth.getToken());
        if (user == null) {
            throw new ApiException(502, "Не удалось загрузить профиль, попробуйте позже");
        }
        HttpSession session = request.getSession(true);
        request.changeSessionId();
        Sessions.start(session, auth.getToken(), user);
    }

    private String validate(RegisterForm form, String password, String confirm) {
        String username = form.getUsername();
        if (username.length() < 3 || username.length() > 50) {
            return "Имя пользователя: от 3 до 50 символов";
        }
        if (form.getFullName().length() > 100) {
            return "Имя и фамилия: не более 100 символов";
        }
        String email = form.getEmail();
        if (email.length() > 100 || !EMAIL.matcher(email).matches()) {
            return "Введите корректный адрес электронной почты";
        }
        if (password == null || password.length() < 6) {
            return "Пароль: не менее 6 символов";
        }
        // BCrypt учитывает только первые 72 байта пароля
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            return "Пароль слишком длинный: не более 72 байт";
        }
        if (!password.equals(confirm)) {
            return "Пароли не совпадают";
        }
        return null;
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
