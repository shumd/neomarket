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
import ru.shumilin.authservice.exception.EmailAlreadyClaimedException;
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
}
