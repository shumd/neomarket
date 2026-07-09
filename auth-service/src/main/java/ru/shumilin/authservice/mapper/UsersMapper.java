package ru.shumilin.authservice.mapper;

import org.springframework.stereotype.Component;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.entity.UsersEntity;
import ru.shumilin.authservice.exception.RoleTypeNotFoundException;

@Component
public class UsersMapper {

    public RegisterResponseDto toRegisterResponseDto(UsersEntity entity){
        if (entity.getRoleType() == null ||
                entity.getRoleType().getNameType() == null)
            throw new IllegalArgumentException("RoleType can not be null");

        return new RegisterResponseDto(
                entity.getId(),
                entity.getLastName(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLogin(),
                entity.getRoleType().getNameType()
        );
    }
}
