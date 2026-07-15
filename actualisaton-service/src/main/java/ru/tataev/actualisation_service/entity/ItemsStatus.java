package ru.tataev.actualisation_service.entity;

import lombok.Getter;
import ru.tataev.actualisation_service.exception.InvalidRequestException;

@Getter
public enum ItemsStatus {
    ITEM_OUT_OF_STACK(32L, "ITEM OUT OF STACK"),
    ITEM_AVAILABLE(31L, "ITEM AVAILABLE");

    private final Long id;
    private final String name;

    ItemsStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ItemsStatus fromValue(Long value) {
        for (ItemsStatus status : values()) {
            if (status.id.equals(value)) {
                return status;
            }
        }
        throw new InvalidRequestException("Недопустимый статус: " + value);
    }
}
