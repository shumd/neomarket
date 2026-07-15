package ru.tataev.basket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tataev.basket_service.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
