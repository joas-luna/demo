package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.example.demo.utils.RegrasValidacao;

@Schema(description = "Payload para criacao/atualizacao de usuario")
public record UsuarioFormDTO(
        @Schema(description = "Nome do usuario", example = "Ana")
        @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
        @Schema(description = "Email do usuario", example = "ana@example.com")
        @NotBlank @Email String email,
        @Schema(description = "Senha com no minimo 8 caracteres", example = "12345678")
        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") String senha,
        @Schema(description = "Pais de origem", example = "Brasil")
        @NotBlank String pais) {
}
