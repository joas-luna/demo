package com.example.demo.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Date dataDeNascimento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String pais;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
