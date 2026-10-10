package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.isu.cityinfra.frontend.security.Sessions;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (Sessions.isLoggedIn(request.getSession(false))) {
            return "redirect:/dashboard";
        }
        return "index";
    }
}
