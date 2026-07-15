package ru.tataev.basket_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tataev.basket_service.dto.AddRequestDto;
import ru.tataev.basket_service.dto.AddResponseDto;
import ru.tataev.basket_service.service.BasketService;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketRestController {

    private final BasketService basketService;

    @PostMapping("/AddItemsInBasket")
    public ResponseEntity<AddResponseDto> add(@Valid @RequestBody AddRequestDto req) {
        AddResponseDto res = basketService.addToBasket(req);
        return ResponseEntity.status(201).body(res);
    }
}
