package ru.tataev.basket_service.entity;

import lombok.Getter;
import ru.tataev.basket_service.exception.InvalidRequestException;

@Getter
public enum OrderStatus {
    ITEM_OUT_OF_STACK(32L, "ITEM OUT OF STACK"),
    ORDER_CONFIRMED(12L, "ORDER CONFIRMED"),
    DRAFT(4L, "DRAFT");

    private final Long id;
    private final String name;

    OrderStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static OrderStatus fromValue(Long value) {
        for (OrderStatus status : values()) {
            if (status.id.equals(value)) {
                return status;
            }
        }
        throw new InvalidRequestException("Недопустимый статус: " + value);
    }
}