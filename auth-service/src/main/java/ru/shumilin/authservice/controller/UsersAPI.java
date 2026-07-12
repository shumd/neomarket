package ru.shumilin.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.shumilin.authservice.dto.request.UpdateBankDetailRequestDto;
import ru.shumilin.authservice.dto.response.ErrorResponseDto;
import ru.shumilin.authservice.dto.response.UpdateBankDetailResponseDto;

import static ru.shumilin.authservice.util.ErrorTitleConstant.*;
import static ru.shumilin.authservice.util.ErrorTitleConstant.INTERNAL_SERVER_ERROR;

@Tag(name = "users-controller", description = "Взаимодействие с данными пользователя")
public interface UsersAPI {
    @Operation(summary = "Добавление/обновление банковских реквизитов пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Реквизиты успешно сохранены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateBankDetailResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = BAD_REQUEST,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = UNAUTHORIZED,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "406",
                    description = NOT_ACCEPTABLE,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = INTERNAL_SERVER_ERROR,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    ResponseEntity<UpdateBankDetailResponseDto> updateBankDetail(String email,
                                                                 UpdateBankDetailRequestDto request);
}
