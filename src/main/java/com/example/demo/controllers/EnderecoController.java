package com.example.demo.controllers;

import com.example.demo.models.dto.EnderecoDTO;
import com.example.demo.services.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios/{usuarioId}/endereco")
@RequiredArgsConstructor
@Tag(name = "Enderecos", description = "Operacoes de endereco vinculadas a um usuario")
public class EnderecoController {
    private final EnderecoService enderecoService;

    @PostMapping
    @Operation(summary = "Criar endereco", description = "Cria o endereco do usuario informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereco criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> criar(@PathVariable Long usuarioId, @Valid @RequestBody EnderecoDTO dto) {
        return ResponseEntity.status(201).body(enderecoService.criar(usuarioId, dto));
    }

    @GetMapping
    @Operation(summary = "Buscar endereco", description = "Retorna o endereco associado ao usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereco encontrado"),
            @ApiResponse(responseCode = "404", description = "Endereco nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(enderecoService.buscarPorUsuario(usuarioId));
    }

    @PutMapping
    @Operation(summary = "Atualizar endereco", description = "Atualiza o endereco associado ao usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereco atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Endereco ou usuario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> atualizar(@PathVariable Long usuarioId, @Valid @RequestBody EnderecoDTO dto) {
        return ResponseEntity.ok(enderecoService.atualizar(usuarioId, dto));
    }

    @DeleteMapping
    @Operation(summary = "Remover endereco", description = "Remove o endereco associado ao usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Endereco removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereco ou usuario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deletar(@PathVariable Long usuarioId) {
        enderecoService.deletar(usuarioId);
        return ResponseEntity.noContent().build();
    }
}