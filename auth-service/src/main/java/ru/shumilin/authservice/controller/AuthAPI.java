package ru.shumilin.authservice.controller;

import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;

public interface AuthAPI {
    RegisterResponseDto register(RegisterRequestDto request);
}
