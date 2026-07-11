package ru.tataev.basket_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
public class BasketRestController {
    @PostMapping("/BuyFromBasket")
    public String buy() {
        return "";
    }
}
