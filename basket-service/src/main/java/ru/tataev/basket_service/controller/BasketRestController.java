package ru.tataev.basket_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tataev.basket_service.dto.DeleteRequestDto;
import ru.tataev.basket_service.dto.DeleteResponseDto;
import ru.tataev.basket_service.service.BasketService;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketRestController {

    private final BasketService basketService;

    @DeleteMapping("/DeleteProductFromBasket")
    public ResponseEntity<DeleteResponseDto> deleteBasket(@Valid @RequestBody DeleteRequestDto req){
        DeleteResponseDto res = basketService.deleteItemsFromBasket(req);
        return ResponseEntity.status(200).body(res);
    }
}
