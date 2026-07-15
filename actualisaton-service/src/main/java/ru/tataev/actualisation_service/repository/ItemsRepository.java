package ru.tataev.actualisation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tataev.actualisation_service.entity.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
}
