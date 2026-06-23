package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de todas as consultas")
public record ConsultaResumoDTO(
        @Schema(example = "10") Integer totalConsultas,
        @Schema(example = "5") Integer consultasCachorros,
        @Schema(example = "3") Integer consultasGatos,
        @Schema(example = "4.5") Double idadeMediaPets
) {
}
