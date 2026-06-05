package com.example.demo.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ConsultaFormDTO(
        @NotNull @Positive Long veterinarioId,
        @NotNull @Positive Long petId,
        @NotBlank String data) {
}
