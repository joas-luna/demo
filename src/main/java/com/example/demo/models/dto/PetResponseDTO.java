package com.example.demo.models.dto;

import com.example.demo.models.entities.Pet;

public record PetResponseDTO(Long id, String nome, String tipo, int idade, Long donoId, String donoNome) {

    public static PetResponseDTO from(Pet pet) {
        return new PetResponseDTO(
                pet.getId(),
                pet.getNome(),
                pet.getTipo(),
                pet.getIdade(),
                pet.getDono() != null ? pet.getDono().getId() : null,
                pet.getDono() != null ? pet.getDono().getNome() : null
        );
    }
}
