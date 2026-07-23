package ru.ecospas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "Введите логин")
    @Size(min = 4, max = 30)
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank(message = "Введите email")
    @Email(message = "Некорректный email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Введите пароль")
    @Size(min = 8, max = 100)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}