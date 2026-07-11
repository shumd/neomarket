package ru.tataev.basket_service.entity;

import lombok.Getter;

@Getter
public enum ItemStatus {
    ORDER_ITEM_PENDING(21L),
    ITEM_OUT_OF_STACK(32L);

    private final Long id;

    ItemStatus(Long id) {
        this.id = id;
    }
}