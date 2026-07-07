package ru.shumilin.authservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.shumilin.authservice.entity.UsersEntity;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends CrudRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findByLogin(String login);
}
