package ru.tataev.basket_service.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ITEM_OUT_OF_STACK(32L),
    DRAFT(4L);

    private final Long id;

    OrderStatus(Long id) {
        this.id = id;
    }

//    public static OrderStatus fromValue(String value){
//        for (OrderStatus status: values()){
//            if (status.id.equals(value)){
//                return status;
//            }
//        }
//        throw new InvalidRequestException("Недопустимый статус: " + value);
//    }
}
