package ru.shumilin.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.shumilin.authservice.dto.request.LoginRequestDto;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.ErrorResponseDto;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;

import static ru.shumilin.authservice.util.ErrorTitleConstant.*;

@Tag(name = "auth-controller", description = "Регистрация и аутентификация пользователя")
public interface AuthAPI {
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Пользователь успешно зарегистрировался",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegisterResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = BAD_REQUEST,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "406",
                    description = NOT_ACCEPTABLE,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = CONFLICT,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = INTERNAL_SERVICE_ERROR,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    ResponseEntity<RegisterResponseDto> register(RegisterRequestDto request);

    ResponseEntity<LoginResponseDto> login(LoginRequestDto request);
}
