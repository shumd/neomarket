package ru.shumilin.authservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.shumilin.authservice.entity.RoleTypeEntity;

public interface RoleTypeRepository extends CrudRepository<RoleTypeEntity, Integer> {
}
