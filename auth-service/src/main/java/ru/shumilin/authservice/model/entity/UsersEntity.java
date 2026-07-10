package ru.shumilin.authservice.model.entity;

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
    private String lastName;

    @NotNull
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @NotNull
    @Column(name = "hash_password", length = 80)
    private String hashPassword;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_type")
    private RoleTypeEntity roleType;

    @Column(name = "bank_detail")
    private String bankDetail;
}
