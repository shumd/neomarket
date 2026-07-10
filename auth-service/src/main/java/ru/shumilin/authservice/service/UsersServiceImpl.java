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
import ru.shumilin.authservice.model.entity.RoleTypeEntity;
import ru.shumilin.authservice.model.entity.UsersEntity;
import ru.shumilin.authservice.exception.EmailAlreadyClaimedException;
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
        if(request == null)
            throw new IllegalArgumentException("Request cant be null");

        log.info("Trying to register user with email: {}", request.email());

        UsersEntity usersEntity = usersRepository.findByEmail(request.email())
                .orElse(null);

        if(usersEntity == null){
            RoleTypeEntity customerRoleTypeEntity = roleTypeRepository
                    .findById(customerRoleTypeId)
                    .orElseThrow(() -> new RoleTypeNotFoundException(customerRoleTypeId));

            usersEntity = UsersEntity.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .hashPassword(passwordEncoder.encode(request.password()))
                    .roleType(customerRoleTypeEntity)
                    .build();

            return usersMapper.toRegisterResponseDto(usersRepository.save(usersEntity));
        } else {
            throw new EmailAlreadyClaimedException(request.email());
        }
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        if(request == null) throw new IllegalArgumentException("Request cant be null");

        log.info("Trying to login user with email: {}", request.email()); // Лучше же не вносить в лог пароль?

        UsersEntity entity = usersRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidLoginDataException(request.email()));

        if (!passwordEncoder.matches(request.password(), entity.getHashPassword())){
            throw new InvalidLoginDataException(request.email());
        }

        return new LoginResponseDto(jwtService.generateToken(entity));
    }
}
