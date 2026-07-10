package ru.shumilin.authservice.exception;

public class RoleTypeNotFoundException extends RuntimeException {
    public RoleTypeNotFoundException(int id) {
        super("Role type with id: %d not found".formatted(id));
    }
}
