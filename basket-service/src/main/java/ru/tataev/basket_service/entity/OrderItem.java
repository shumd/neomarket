package ru.tataev.basket_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO: Items еще не готовы в бд, поэтому пока пропускаем их
//    @Column(name = "id_items", nullable = false)
//    private Long idItems;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "initial_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialPrice;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "comment")
    private String comment;
}