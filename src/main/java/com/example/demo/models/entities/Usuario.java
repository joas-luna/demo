package com.example.demo.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String pais;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL)
    private List<Pet> pets;

    public Usuario(String nome, String email, String senha, String pais){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.pais = pais;
    }
}
