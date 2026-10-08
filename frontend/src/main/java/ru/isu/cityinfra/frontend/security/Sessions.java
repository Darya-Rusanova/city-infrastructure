package ru.isu.cityinfra.frontend.security;

import jakarta.servlet.http.HttpSession;
import ru.isu.cityinfra.frontend.dto.UserDto;

/**
 * Единое место для работы с данными пользователя в HTTP-сессии.
 * Токен выдает Auth Service, фронтенд только хранит его и передает сервисам.
 */
public final class Sessions {

    public static final String TOKEN = "token";
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "fullName";
    public static final String ROLE = "role";
    public static final String ROLE_NAME = "roleName";
    public static final String PENDING_PAYMENTS = "pendingPayments";

    private Sessions() {
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute(TOKEN) != null;
    }

    public static String token(HttpSession session) {
        return (String) session.getAttribute(TOKEN);
    }

    public static Integer userId(HttpSession session) {
        return (Integer) session.getAttribute(USER_ID);
    }

    public static boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute(ROLE));
    }

    public static void start(HttpSession session, String token, UserDto user) {
        String fullName = user.getFullName();
        if (fullName == null || fullName.isBlank()) {
            fullName = user.getUsername();
        }
        session.setAttribute(TOKEN, token);
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(USERNAME, user.getUsername());
        session.setAttribute(FULL_NAME, fullName);
        session.setAttribute(ROLE, user.getRole());
        session.setAttribute(ROLE_NAME, user.getNameRole());
        session.removeAttribute(PENDING_PAYMENTS);
    }
}
