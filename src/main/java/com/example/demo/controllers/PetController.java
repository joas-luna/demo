package com.example.demo.controllers;

import com.example.demo.models.dto.PetFormDTO;
import com.example.demo.models.dto.PetResponseDTO;
import com.example.demo.services.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Operacoes de gerenciamento de pets")
public class PetController {
    private final PetService petService;

    @PostMapping
    @Operation(summary = "Criar pet", description = "Cria um novo pet vinculado a um usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Dono nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> criar(@RequestBody @Valid PetFormDTO dto) {
        return ResponseEntity.status(201).body(petService.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar pets", description = "Lista todos os pets ou filtra por donoId")
    @ApiResponse(responseCode = "200", description = "Lista de pets retornada com sucesso")
    public ResponseEntity<List<PetResponseDTO>> listar(@RequestParam(required = false) Long donoId) {
        return ResponseEntity.ok(petService.listarPets(donoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pet por ID", description = "Retorna um pet pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet encontrado"),
            @ApiResponse(responseCode = "404", description = "Pet nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(petService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pet", description = "Atualiza um pet pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pet ou dono nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid PetFormDTO dto) {
        return ResponseEntity.ok(petService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover pet", description = "Remove um pet pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        petService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}