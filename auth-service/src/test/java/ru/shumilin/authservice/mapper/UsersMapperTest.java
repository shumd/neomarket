package ru.shumilin.authservice.mapper;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.entity.RoleTypeEntity;
import ru.shumilin.authservice.entity.UsersEntity;

import java.util.UUID;

public class UsersMapperTest {
    private final UsersMapper usersMapper = new UsersMapper();

    @Test
    public void toRegisterResponseDto_WithValidUsersEntity_returnRegisterResponseDto(){
        UUID uuid = UUID.randomUUID();
        RoleTypeEntity roleType = RoleTypeEntity.builder()
                .id(2)
                .nameType("Customer")
                .permissions("CUSTOMER")
                .activity(true)
                .comment("test")
                .build();
        UsersEntity usersEntity = UsersEntity.builder()
                .id(uuid)
                .firstName("test firstName")
                .middleName("test middleName")
                .lastName("test lastName")
                .login("test login")
                .hashPassword("123")
                .roleType(roleType)
                .build();
        RegisterResponseDto expected = new RegisterResponseDto(
                uuid,
                "test lastName",
                "test firstName",
                "test middleName",
                "test login",
                "Customer"
        );

        Assertions.assertEquals(expected,
                usersMapper.toRegisterResponseDto(usersEntity));
    }
}
