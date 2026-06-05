package com.example.demo.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.example.demo.utils.RegrasValidacao;

public record UsuarioFormDTO(
        @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") String senha,
        @NotBlank String pais) {
}
