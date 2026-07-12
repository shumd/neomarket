package ru.shumilin.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shumilin.authservice.dto.request.LoginRequestDto;
import ru.shumilin.authservice.dto.request.RegisterRequestDto;
import ru.shumilin.authservice.dto.request.UpdateBankDetailRequestDto;
import ru.shumilin.authservice.dto.response.LoginResponseDto;
import ru.shumilin.authservice.dto.response.LogoutResponseDto;
import ru.shumilin.authservice.dto.response.RegisterResponseDto;
import ru.shumilin.authservice.dto.response.UpdateBankDetailResponseDto;
import ru.shumilin.authservice.exception.*;
import ru.shumilin.authservice.model.LogoutStatus;
import ru.shumilin.authservice.model.entity.RoleTypeEntity;
import ru.shumilin.authservice.model.entity.UsersEntity;
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
        if (request == null)
            throw new IllegalArgumentException("Request cant be null");

        log.info("Trying to register user with email: {}", request.email());

        UsersEntity usersEntity = usersRepository.findByEmail(request.email())
                .orElse(null);

        if (usersEntity == null) {
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
        if (request == null) throw new IllegalArgumentException("Request cant be null");

        log.info("Trying to login user with email: {}", request.email()); // Лучше же не вносить в лог пароль?

        UsersEntity entity = usersRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidLoginDataException(request.email()));

        if (!passwordEncoder.matches(request.password(), entity.getHashPassword())) {
            throw new InvalidLoginDataException(request.email());
        }

        return new LoginResponseDto(jwtService.generateToken(entity));
    }

    @Override
    public LogoutResponseDto logout(String token) {
        jwtService.addToBlackList(token);

        return new LogoutResponseDto(
                "Вы успешно вышли из системы", // Надо ли message вынести в отдельное статическое поле, как ErrorTitleConstant?
                LogoutStatus.SUCCESS);
    }

    @Override
    @Transactional
    public UpdateBankDetailResponseDto updateBankDetail(String email, UpdateBankDetailRequestDto request) {
        if (email == null || email.isBlank())
            throw new InvalidUpdateBankDetailDataException("Email cant be blank");

        if (request == null || request.bankDetail() == null || request.bankDetail().isBlank()) {
            throw new InvalidUpdateBankDetailDataException("UpdateBankDetailRequestDto cant be blank");
        }

        log.info("Trying to update bank detail for user: {}", email);

        UsersEntity usersEntity = usersRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException(email));

        usersEntity.setBankDetail(request.bankDetail());

        return new UpdateBankDetailResponseDto(usersRepository.save(usersEntity).getBankDetail());
    }
}
