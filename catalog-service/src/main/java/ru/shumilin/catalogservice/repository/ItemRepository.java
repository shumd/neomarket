package ru.shumilin.catalogservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shumilin.catalogservice.entity.ItemEntity;

import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Integer> {
    Optional<ItemEntity> findByIdAndStatusId(Integer id, Integer statusId);
}