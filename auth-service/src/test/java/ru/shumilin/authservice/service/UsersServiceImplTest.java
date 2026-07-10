package ru.shumilin.authservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shumilin.authservice.dto.request.LoginRequestDto;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.model.entity.RoleTypeEntity;
import ru.shumilin.authservice.model.entity.UsersEntity;
import ru.shumilin.authservice.exception.EmailAlreadyClaimedException;
import ru.shumilin.authservice.exception.InvalidLoginDataException;
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

    @Mock
    private JwtService jwtService;

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
    public void register_withClaimedEmail_throwEmailAlreadyClaimedException(){
        when(usersRepository.findByEmail("email")).thenReturn(Optional.of(new UsersEntity()));
        Assertions.assertThrows(EmailAlreadyClaimedException.class,
                () -> usersService.register(getRegisterRequestDto()));
    }

    @Test
    public void register_whenCustomerRoleTypeNotFound_throwRoleTypeNotFound(){
        when(usersRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(roleTypeRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(RoleTypeNotFoundException.class,
                () -> usersService.register(getRegisterRequestDto()));
    }

    @Test
    void register_withNullRequest_throwIllegalArgumentException(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> usersService.register(null));
    }

    @Test
    public void login_withValidData_returnLoginResponseDto(){
        String token = "test token";

        LoginRequestDto requestDto = new LoginRequestDto(
                "login123456",
                "testPassword");

        RoleTypeEntity roleTypeEntity = RoleTypeEntity.builder()
                .id(1)
                .nameType("test permission name")
                .permissions("test permissions")
                .activity(true)
                .comment("test comment")
                .build();

        UsersEntity usersEntity = UsersEntity
                .builder()
                .id(UUID.randomUUID())
                .firstName("test first name")
                .lastName("test last name")
                .email("email@mail.ru")
                .hashPassword("testHashPassword")
                .roleType(roleTypeEntity)
                .bankDetail("test bank detail")
                .build();

        when(passwordEncoder.matches("testPassword","testHashPassword")).thenReturn(true);
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(usersEntity));
        when(jwtService.generateToken(any())).thenReturn(token);

        Assertions.assertEquals(new LoginResponseDto(token), usersService.login(requestDto));
    }

    @Test
    void login_withWrongLogin_throwInvalidLoginDataException(){
        LoginRequestDto requestDto = new LoginRequestDto(
                "test",
                "test password"
        );

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidLoginDataException.class,
                () -> usersService.login(requestDto));
    }

    @Test
    void login_withWrongPassword_throwInvalidLoginDataException(){
        LoginRequestDto requestDto = new LoginRequestDto(
                "test",
                "test password"
        );

        when(usersRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(UsersEntity.builder().hashPassword("123").build()));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(false);

        Assertions.assertThrows(InvalidLoginDataException.class,
                () -> usersService.login(requestDto));
    }

    @Test
    void login_withNullRequest_throwIllegalArgumentException(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> usersService.login(null));
    }

    private RegisterRequestDto getRegisterRequestDto(){
        return new RegisterRequestDto(
                "Ivanov",
                "Ivan",
                "email",
                "123");
    }
}
