package ru.shumilin.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role_type")
public class RoleTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "name_type", length = 100)
    private String nameType;

    @NotNull
    private String permissions;

    @NotNull
    private Boolean activity;

    private String comment;
}