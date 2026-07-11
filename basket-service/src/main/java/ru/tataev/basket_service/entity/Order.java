package ru.tataev.basket_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "id_user", nullable = false)
    private UUID idUser;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private Status status;

    @Column(name = "date_order", nullable = false)
    private LocalDate dateOrder;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "id_shipping", nullable = false)
    private Integer idShipping;

    @Column(name = "id_comission", nullable = false)
    private Integer idComission;
}
