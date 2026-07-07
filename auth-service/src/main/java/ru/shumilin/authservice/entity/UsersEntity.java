package ru.shumilin.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "first_name", length = 200)
    private String firstName;

    @NotNull
    @Column(name = "last_name", length = 200)
    private String last_name;

    @NotNull
    @Column(name = "login", length = 50, unique = true)
    private String login;

    @NotNull
    @ManyToOne
    private RoleTypeEntity roleType;
}
