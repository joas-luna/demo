package com.example.demo.models.entities;


import com.example.demo.models.dto.PetDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;
    private Integer idade;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario dono;

    public PetDTO toDTO() {
        return new PetDTO(nome, idade, tipo);
    }
}
