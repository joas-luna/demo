package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de endereco do usuario")
public record EnderecoDTO(
        @Schema(example = "Rua A")
        @NotBlank String rua,
        @Schema(example = "123")
        @NotBlank String numero,
        @Schema(example = "Centro")
        @NotBlank String bairro,
        @Schema(example = "Joao Pessoa")
        @NotBlank String cidade,
        @Schema(description = "UF com 2 caracteres", example = "PB")
        @NotBlank @Size(min = 2, max = 2, message = "Estado tem que ter 2 caracteres") String estado,
        @Schema(example = "58000-000")
        @NotBlank String cep) {
}
