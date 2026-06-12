package com.example.demo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import com.example.demo.utils.RegrasValidacao;

@Schema(description = "Payload para criacao/atualizacao de pet")
public record PetFormDTO(
                @Schema(example = "Rex")
                @NotBlank @Pattern(regexp = RegrasValidacao.APENAS_LETRAS, message = "Nome não pode conter números") String nome,
                @Schema(example = "Cachorro")
                @NotBlank String tipo,
                @Schema(example = "3")
                @NotNull @PositiveOrZero Integer idade,
                @Schema(description = "ID do usuario dono do pet", example = "1")
                @NotNull @Positive Long donoId) {
}