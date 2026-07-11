package ru.tataev.basket_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tataev.basket_service.dto.BuyResponseDto;
import ru.tataev.basket_service.dto.BuyRequestDto;
import ru.tataev.basket_service.service.BasketService;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketRestController {

    private final BasketService basketService;

    @PostMapping("/BuyFromBasket")
    public ResponseEntity<BuyResponseDto> buy(@Valid @RequestBody BuyRequestDto req) {
        BuyResponseDto res = basketService.updateBasket(req);
        return ResponseEntity.status(200).body(res);
    }
}
