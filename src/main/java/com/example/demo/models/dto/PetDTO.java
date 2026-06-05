package com.example.demo.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.example.demo.utils.RegrasValidacao;

public record PetDTO(@NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
        int idade, String tipo) {
}
