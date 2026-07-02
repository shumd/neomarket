package ru.shumilin.catalogservice.exception;

public class ItemNotActiveException extends RuntimeException {
    public ItemNotActiveException(int id) {
        super("Item with id %d is not active".formatted(id));
    }
}
