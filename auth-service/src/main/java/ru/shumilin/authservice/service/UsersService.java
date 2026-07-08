package ru.shumilin.authservice.service;

import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;

public interface UsersService {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
}
