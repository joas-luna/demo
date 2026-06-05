package com.example.demo.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.example.demo.utils.RegrasValidacao;

public record VeterinarioDTO(
        @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
        @NotBlank String crmv) {
}
