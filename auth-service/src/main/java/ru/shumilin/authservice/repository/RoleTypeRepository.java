package ru.shumilin.authservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.shumilin.authservice.model.entity.RoleTypeEntity;

public interface RoleTypeRepository extends CrudRepository<RoleTypeEntity, Integer> {
}
