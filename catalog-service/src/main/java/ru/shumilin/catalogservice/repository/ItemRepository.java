package ru.shumilin.catalogservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shumilin.catalogservice.entity.ItemEntity;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Integer> {
}
