package ru.shumilin.catalogservice.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(int id) {
        super("Item with id %d not found".formatted(id));
    }
}
