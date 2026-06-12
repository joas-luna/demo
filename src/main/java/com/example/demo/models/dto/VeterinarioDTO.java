package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.example.demo.utils.RegrasValidacao;

@Schema(description = "Dados de veterinario")
public record VeterinarioDTO(
        @Schema(example = "Dr Silva")
        @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
        @Schema(example = "PB-1234")
        @NotBlank String crmv) {
}
