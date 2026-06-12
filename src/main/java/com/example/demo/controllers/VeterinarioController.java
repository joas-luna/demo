package com.example.demo.controllers;

import com.example.demo.models.dto.VeterinarioDTO;
import com.example.demo.services.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veterinarios")
@RequiredArgsConstructor
@Tag(name = "Veterinarios", description = "Operacoes de gerenciamento de veterinarios")
public class VeterinarioController {
    private final VeterinarioService veterinarioService;

    @PostMapping
    @Operation(summary = "Criar veterinario", description = "Cadastra um novo veterinario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veterinario criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> criar(@RequestBody @Valid VeterinarioDTO dto) {
        return new ResponseEntity<>(veterinarioService.cadastrar(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar veterinarios", description = "Retorna todos os veterinarios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de veterinarios retornada com sucesso")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(veterinarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veterinario por ID", description = "Retorna um veterinario pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinario encontrado"),
            @ApiResponse(responseCode = "404", description = "Veterinario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.encontrarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veterinario", description = "Atualiza os dados de um veterinario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinario atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Veterinario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid VeterinarioDTO dto) {
        return ResponseEntity.ok(veterinarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover veterinario", description = "Remove um veterinario pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veterinario removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        veterinarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
