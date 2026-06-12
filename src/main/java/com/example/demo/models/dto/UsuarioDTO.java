package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resposta de usuario")
public record UsuarioDTO(
                @Schema(example = "Ana")
                String nome,
                @Schema(example = "ana@example.com")
                String email,
                @Schema(example = "12345678")
                String senha,
                @Schema(example = "Brasil")
                String pais,
                EnderecoDTO endereco,
                List<PetDTO> pets) {
}
