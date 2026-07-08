package ru.shumilin.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.entity.RoleTypeEntity;
import ru.shumilin.authservice.entity.UsersEntity;
import ru.shumilin.authservice.exception.LoginAlreadyClaimedException;
import ru.shumilin.authservice.exception.RoleTypeNotFoundException;
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

    @Value("${app.customer-role-type-id}")
    private int customerRoleTypeId;

    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {
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
}
