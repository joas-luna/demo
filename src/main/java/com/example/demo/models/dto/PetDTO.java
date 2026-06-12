package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.example.demo.utils.RegrasValidacao;

@Schema(description = "Resumo de pet")
public record PetDTO(
        @Schema(example = "Nina")
        @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
        @Schema(example = "2") int idade,
        @Schema(example = "Gato") String tipo) {
}
