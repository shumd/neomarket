package ru.shumilin.authservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.entity.RoleTypeEntity;
import ru.shumilin.authservice.entity.UsersEntity;
import ru.shumilin.authservice.exception.LoginAlreadyClaimedException;
import ru.shumilin.authservice.exception.RoleTypeNotFoundException;
import ru.shumilin.authservice.mapper.UsersMapper;
import ru.shumilin.authservice.repository.RoleTypeRepository;
import ru.shumilin.authservice.repository.UsersRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceImplTest {
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RoleTypeRepository roleTypeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private UsersServiceImpl usersService;

    @Test
    public void register_withValidArguments_returnRegisterResponseDto(){
        int customerRoleTypeId = 2;
        ReflectionTestUtils.setField(
                usersService,
                "customerRoleTypeId",
                customerRoleTypeId);

        RoleTypeEntity roleTypeEntity = RoleTypeEntity.builder()
                .id(customerRoleTypeId)
                .nameType("Customer")
                .permissions("test")
                .activity(true)
                .comment("test comment")
                .build();

        RegisterResponseDto registerResponseDto = new RegisterResponseDto(
                UUID.randomUUID(),
                "Ivanov",
                "Ivan",
                "email",
                "customer");

        when(usersRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded password");
        when(roleTypeRepository.findById(customerRoleTypeId)).thenReturn(Optional.of(roleTypeEntity));
        when(usersMapper.toRegisterResponseDto(any())).thenReturn(registerResponseDto);

        Assertions.assertEquals(registerResponseDto, usersService.register(getRegisterRequestDto()));
    }

    @Test
    public void register_withClaimedLogin_throwLoginAlreadyClaimedException(){
        when(usersRepository.findByEmail("email")).thenReturn(Optional.of(new UsersEntity()));
        Assertions.assertThrows(LoginAlreadyClaimedException.class,
                () -> usersService.register(getRegisterRequestDto()));
    }

    @Test
    public void register_whenCustomerRoleTypeNotFound_throwRoleTypeNotFound(){
        when(usersRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(roleTypeRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(RoleTypeNotFoundException.class,
                () -> usersService.register(getRegisterRequestDto()));
    }

    private RegisterRequestDto getRegisterRequestDto(){
        return new RegisterRequestDto(
                "Ivanov",
                "Ivan",
                "email",
                "123");
    }
}
