package ru.isu.cityinfra.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        log.warn("NotFound: {}", e.getMessage());
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", e.getMessage(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException e) {
        log.warn("Conflict: {}", e.getMessage());
        return build(HttpStatus.CONFLICT, "CONFLICT", e.getMessage(), null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException e) {
        log.warn("Unauthorized: {}", e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", e.getMessage(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException e) {
        log.warn("Forbidden: {}", e.getMessage());
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe ->
                errors.put(fe.getField(), fe.getDefaultMessage()));
        log.warn("Validation error: {}", errors);
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "Ошибка валидации", errors);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error,
                                                String message, Map<String, String> errors) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .errors(errors)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}