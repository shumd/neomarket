package ru.shumilin.catalogservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CategoryEntity {
    @Id
    private Integer id;

    @Column(length = 100)
    private String name;

    @Column(name = "id_status")
    private Integer statusId;

    private String comment;
}
