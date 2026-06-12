package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de consulta")
public record ConsultaDTO(
        @Schema(example = "1") Long id,
        VeterinarioDTO veterinario,
        PetDTO pet,
        @Schema(example = "2026-06-10T10:00") String data) {
}
