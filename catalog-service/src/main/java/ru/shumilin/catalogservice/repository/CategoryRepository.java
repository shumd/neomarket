package ru.shumilin.catalogservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shumilin.catalogservice.model.entity.CategoryEntity;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findAllByStatusIdOrderByNameAsc(Integer statusId);
}
