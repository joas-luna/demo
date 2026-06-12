package com.example.demo.models.dto;

import com.example.demo.models.entities.Pet;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de pet")
public record PetResponseDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "Rex") String nome,
        @Schema(example = "Cachorro") String tipo,
        @Schema(example = "3") int idade,
        @Schema(example = "1") Long donoId,
        @Schema(example = "Carlos") String donoNome) {

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
