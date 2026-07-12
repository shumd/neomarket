package ru.tataev.basket_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tataev.basket_service.dto.OrderResponseDto;
import ru.tataev.basket_service.service.BasketService;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketRestController {
    private final BasketService basketService;

    @GetMapping("OpenBasket/{id}")
    public ResponseEntity<OrderResponseDto> get(@PathVariable String id){
        OrderResponseDto res = basketService.getBasketById(id);
        return ResponseEntity.status(200).body(res);
    }
}
