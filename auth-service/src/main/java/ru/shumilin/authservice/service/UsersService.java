package ru.shumilin.authservice.service;

import ru.shumilin.authservice.dto.request.LoginRequestDto;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.LogoutResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;

public interface UsersService {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    LogoutResponseDto logout();
}
