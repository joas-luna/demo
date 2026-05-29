package com.example.demo.models.entities;

import com.example.demo.models.dto.ConsultaDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "consultas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_consulta_vet_pet_datahora",
                columnNames = {"veterinario_id", "pet_id", "data_hora"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public Consulta (Veterinario veterinario, Pet pet, LocalDateTime dataHora) {
        this.veterinario = veterinario;
        this.pet = pet;
        this.dataHora = dataHora;
    }

    public ConsultaDTO toDTO(){
        return new ConsultaDTO(
                id,
                veterinario.toDTO(),
                pet.toDTO(),
                dataHora.toString());
    }
}

