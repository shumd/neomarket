package ru.shumilin.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shumilin.authservice.dto.request.LoginRequestDto;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.entity.RoleTypeEntity;
import ru.shumilin.authservice.entity.UsersEntity;
import ru.shumilin.authservice.exception.LoginAlreadyClaimedException;
import ru.shumilin.authservice.exception.RoleTypeNotFoundException;
import ru.shumilin.authservice.exception.InvalidLoginDataException;
import ru.shumilin.authservice.mapper.UsersMapper;
import ru.shumilin.authservice.repository.RoleTypeRepository;
import ru.shumilin.authservice.repository.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final RoleTypeRepository roleTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersMapper usersMapper;
    private final JwtService jwtService;

    @Value("${app.customer-role-type-id}")
    private int customerRoleTypeId;

    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {
        if(request == null) throw new IllegalArgumentException("Request cant be null");

        log.info("Trying to register user with login: {}", request.login());

        UsersEntity usersEntity = usersRepository.findByLogin(request.login())
                .orElse(null);

        if(usersEntity == null){
            RoleTypeEntity customerRoleTypeEntity = roleTypeRepository
                    .findById(customerRoleTypeId)
                    .orElseThrow(() -> new RoleTypeNotFoundException(customerRoleTypeId));

            usersEntity = UsersEntity.builder()
                    .firstName(request.firstName())
                    .middleName(request.middleName())
                    .lastName(request.lastName())
                    .login(request.login())
                    .hashPassword(passwordEncoder.encode(request.password()))
                    .roleType(customerRoleTypeEntity)
                    .build();

            usersRepository.save(usersEntity);

            return usersMapper.toRegisterResponseDto(usersEntity);
        } else {
            throw new LoginAlreadyClaimedException(request.login());
        }
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        if(request == null) throw new IllegalArgumentException("Request cant be null");

        log.info("Trying to login user with login: {}", request.login()); // Лучше же не вносить в лог пароль?

        UsersEntity entity = usersRepository.findByLogin(request.login())
                .orElseThrow(() -> new InvalidLoginDataException(request.login()));

        if (!passwordEncoder.matches(request.password(), entity.getHashPassword())){
            throw new InvalidLoginDataException(request.login());
        }

        return new LoginResponseDto(jwtService.generateToken(entity));
    }
}
