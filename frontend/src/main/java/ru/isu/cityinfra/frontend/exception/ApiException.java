package ru.isu.cityinfra.frontend.exception;

/**
 * Ошибка при обращении к бэкенд-сервису. Содержит HTTP-статус и текст, который можно показать пользователю.
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public boolean isUnauthorized() {
        return status == 401;
    }
}
