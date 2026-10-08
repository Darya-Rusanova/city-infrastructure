package ru.isu.cityinfra.frontend.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Достает понятное сообщение из JSON-ответа с ошибкой. Все сервисы отвечают в похожем формате:
 * либо поле "message", либо карта "errors" с ошибками полей формы.
 */
public final class ApiErrors {

    private static final Pattern ERRORS_BLOCK = Pattern.compile("\"errors\"\\s*:\\s*\\{([^}]*)\\}");
    private static final Pattern PAIR = Pattern.compile("\"[^\"]+\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
    private static final Pattern MESSAGE = Pattern.compile("\"message\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");

    private ApiErrors() {
    }

    public static String toMessage(int status, String body) {
        if (body != null && !body.isBlank()) {
            Matcher block = ERRORS_BLOCK.matcher(body);
            if (block.find()) {
                List<String> parts = new ArrayList<>();
                Matcher pair = PAIR.matcher(block.group(1));
                while (pair.find()) {
                    parts.add(unescape(pair.group(1)));
                }
                if (!parts.isEmpty()) {
                    return String.join("; ", parts);
                }
            }
            Matcher message = MESSAGE.matcher(body);
            if (message.find()) {
                String text = unescape(message.group(1));
                if (!text.isBlank()) {
                    return text;
                }
            }
        }
        return defaultMessage(status);
    }

    private static String defaultMessage(int status) {
        return switch (status) {
            case 400 -> "Проверьте введенные данные";
            case 401 -> "Требуется вход в систему";
            case 403 -> "Недостаточно прав для этого действия";
            case 404 -> "Ничего не найдено";
            case 409 -> "Действие невозможно в текущем состоянии";
            default -> status >= 500
                    ? "Ошибка на стороне сервиса, попробуйте позже"
                    : "Не удалось выполнить запрос";
        };
    }

    private static String unescape(String text) {
        return text.replace("\\\"", "\"").replace("\\\\", "\\").replace("\\n", " ");
    }
}
