package ru.shumilin.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shumilin.authservice.dto.request.UpdateBankDetailRequestDto;
import ru.shumilin.authservice.dto.response.UpdateBankDetailResponseDto;
import ru.shumilin.authservice.service.UsersService;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UsersRestController implements UsersAPI{
    private final UsersService usersService;

    @Override
    @PutMapping(value = "/me/bank-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateBankDetailResponseDto> updateBankDetail(@AuthenticationPrincipal String email,
                                                                        @Valid @RequestBody UpdateBankDetailRequestDto request) {
        return ResponseEntity.ok(usersService.updateBankDetail(email, request));
    }
}
