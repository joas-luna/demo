package com.example.demo.controllers;

import com.example.demo.models.dto.EnderecoDTO;
import com.example.demo.services.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Endereços", description = "Gerenciamento de endereços dos usuários")
public class EnderecoController {
    private final EnderecoService enderecoService;

    @PostMapping
    @Operation(summary = "Criar endereço", description = "Cadastra um endereço para um usuário específico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<?> criar(@PathVariable Long usuarioId, @Valid @RequestBody EnderecoDTO dto) {
        try {
            return ResponseEntity.status(201).body(enderecoService.criar(usuarioId, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Buscar endereço", description = "Retorna o endereço vinculado a um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou Endereço não encontrado")
    })
    public ResponseEntity<?> buscar(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(enderecoService.buscarPorUsuario(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "Atualizar endereço", description = "Atualiza o endereço de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou Endereço não encontrado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long usuarioId, @RequestBody EnderecoDTO dto) {
        try {
            return ResponseEntity.ok(enderecoService.atualizar(usuarioId, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "Deletar endereço", description = "Remove o endereço vinculado a um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou Endereço não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long usuarioId) {
        try {
            enderecoService.deletar(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}