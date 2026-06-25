package ru.shumilin.catalogservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_ware_operation")
    private Integer wareOperationId;

    @Column(name = "id_order")
    private Integer orderId;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "date_registration")
    private LocalDate registrationDate;

    @Column(name = "id_org_supplier")
    private Integer orgSupplierId;

    private Integer rating;

    private Integer quantity;

    @Column(length = 50)
    private String status;     //TODO Планируется замена на statusId

    private String description;

    private String comment;

    @Column(name = "id_category")
    private Integer categoryId;
}