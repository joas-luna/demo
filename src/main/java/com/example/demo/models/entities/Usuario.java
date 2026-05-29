package com.example.demo.models.entities;

import com.example.demo.models.dto.EnderecoDTO;
import com.example.demo.models.dto.PetDTO;
import com.example.demo.models.dto.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    @Column(unique = true)
    private String email;
    private String senha;
    private String pais;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private Endereco endereco;

    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Pet> pets;

    public Usuario(String nome, String email, String senha, String pais) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.pais = pais;
    }

    public UsuarioDTO toDTO() {
        EnderecoDTO enderecoDTO = endereco != null ? endereco.toDTO() : null;
//        List<PetDTO> petsDTO;
//        if (pets != null) {
//            for (Pet pet : pets) {
//                petsDTO.add(pet.toDTO());
//            }
//            petsDTO = pets.stream().map(pet -> pet.toDTO()).toList();
//            petsDTO = pets.stream().map(Pet::toDTO).toList();
//
//        } else {
//            petsDTO = new ArrayList<>();
//        }

        List<PetDTO> petsDTO = pets == null
            ? new ArrayList<>()
            : pets.stream().map(Pet::toDTO).toList();
        return new UsuarioDTO(nome, email, senha, pais, enderecoDTO, petsDTO);
    }
}
