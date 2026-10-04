package ru.isu.cityinfra.auth.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) { super(message); }
}