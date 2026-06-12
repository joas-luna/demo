package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Payload para criacao/atualizacao de consulta")
public record ConsultaFormDTO(
        @Schema(description = "ID do veterinario", example = "1")
        @NotNull @Positive Long veterinarioId,
        @Schema(description = "ID do pet", example = "1")
        @NotNull @Positive Long petId,
        @Schema(description = "Data e hora da consulta em formato ISO local", example = "2026-06-10T10:00:00")
        @NotBlank String data) {
}
