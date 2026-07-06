package ru.shumilin.catalogservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shumilin.catalogservice.model.entity.ItemEntity;
import ru.shumilin.catalogservice.model.projection.ItemWithSupplierProjection;


import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Integer> {
    Optional<ItemEntity> findByIdAndStatusId(Integer id, Integer statusId);
    @Query(value = """
            SELECT i.id, i.name, i.price, o.name AS supplier_name, i.quantity
            FROM CATALOG.item i
            JOIN users.organization o ON i.id_org_supplier = o.id
            WHERE i.name ILIKE CONCAT('%', :name, '%')
                        AND (:categoryId IS NULL OR i.id_category = :categoryId)
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM catalog.item i
                    WHERE i.name ILIKE CONCAT('%', :name, '%')
                                        AND (:categoryId IS NULL OR i.id_category = :categoryId)
                    """,
            nativeQuery = true)
    Page<ItemWithSupplierProjection> findAllByName(@Param("name") String name,
                                                   @Param("categoryId") Integer categoryId,
                                                   Pageable pageable);
}