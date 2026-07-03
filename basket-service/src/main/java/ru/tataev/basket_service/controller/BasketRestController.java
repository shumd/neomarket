package ru.tataev.basket_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tataev.basket_service.dto.BasketResponseDto;
import ru.tataev.basket_service.dto.CreateRequestDto;
import ru.tataev.basket_service.service.BasketService;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketRestController {

    private final BasketService basketService;

    @PostMapping("/BuyFromBasket")
    public ResponseEntity<BasketResponseDto> buy(@Valid @RequestBody CreateRequestDto req) {
        BasketResponseDto res = basketService.createBasket(req);
        return ResponseEntity.status(201).body(res);
    }
}
