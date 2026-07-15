package ru.tataev.actualisation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tataev.actualisation_service.entity.Order;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByIdUser(UUID idUser);
    Optional<Order> findByIdUser(UUID idUser);
}
