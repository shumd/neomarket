package ru.tataev.basket_service.entity;

import lombok.Getter;
import ru.tataev.basket_service.exception.InvalidRequestException;

@Getter
public enum OrderStatus {
    NOT_CREATED("1"),
    NOT_PAID("2"),
    SUCCESS("3");

    private final String id;

    OrderStatus(String id) {
        this.id = id;
    }

    public static OrderStatus fromValue(String value){
        for (OrderStatus status: values()){
            if (status.id.equals(value)){
                return status;
            }
        }
        throw new InvalidRequestException("Недопустимый статус: " + value);
    }
}
