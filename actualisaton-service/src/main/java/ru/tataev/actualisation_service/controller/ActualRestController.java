package ru.tataev.actualisation_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tataev.actualisation_service.dto.CheckRequestDto;
import ru.tataev.actualisation_service.dto.CheckResponseDto;
import ru.tataev.actualisation_service.service.ActualService;

@RestController
@RequestMapping("/actual")
@RequiredArgsConstructor
public class ActualRestController {
    private final ActualService actualService;

    @PostMapping("/CheckCountItems")
    public ResponseEntity<CheckResponseDto> check(@Valid @RequestBody CheckRequestDto req){
        CheckResponseDto res = actualService.checkStatus(req);
        return ResponseEntity.status(200).body(res);
    }

    @PostMapping("/ChangeCountItems")
    public ResponseEntity<CheckResponseDto> change(@Valid @RequestBody CheckRequestDto req){
        CheckResponseDto res = actualService.changeCount(req);
        return ResponseEntity.status(200).body(res);
    }
}
