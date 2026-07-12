package ru.tataev.basket_service.entity;

import lombok.Getter;
import ru.tataev.basket_service.exception.InvalidRequestException;

@Getter
public enum ItemStatus {
    ORDER_ITEM_PENDING(21L , "ORDER ITEM PENDING");

    private final Long id;
    private final String name;

    ItemStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ItemStatus fromValue(Long value) {
        for (ItemStatus status : values()) {
            if (status.id.equals(value)) {
                return status;
            }
        }
        throw new InvalidRequestException("Недопустимый статус: " + value);
    }
}