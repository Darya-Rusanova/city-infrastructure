package ru.isu.cityinfra.frontend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApiException.class)
    public String handleApiException(ApiException e, HttpServletRequest request,
                                     HttpServletResponse response, Model model) {
        if (e.isUnauthorized()) {
            // токен истек или недействителен: сбрасываем сессию и просим войти заново
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "redirect:/login?expired";
        }
        int status = e.getStatus() >= 400 && e.getStatus() <= 599 ? e.getStatus() : 500;
        response.setStatus(status);
        model.addAttribute("status", status);
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
