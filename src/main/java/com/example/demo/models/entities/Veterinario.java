package com.example.demo.models.entities;

import com.example.demo.models.dto.VeterinarioDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "veterinarios")
@Getter
@Setter
@NoArgsConstructor
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String crmv;

    public Veterinario(String nome, String crmv) {
        this.nome = nome;
        this.crmv = crmv;
    }

    public VeterinarioDTO toDTO(){
        return new VeterinarioDTO(nome, crmv);
    }
}
