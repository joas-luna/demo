package com.example.demo.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import com.example.demo.utils.RegrasValidacao;

public record PetFormDTO(
                @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
                @NotBlank String tipo,
                @NotNull @PositiveOrZero Integer idade,
                @NotNull @Positive Long donoId) {
}