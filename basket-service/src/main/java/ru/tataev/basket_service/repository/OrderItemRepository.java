package ru.tataev.basket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tataev.basket_service.entity.OrderItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_id(Long id);
    void deleteByOrder_id(Long id);
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
