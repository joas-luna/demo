package com.example.demo.controllers;

import com.example.demo.models.dto.PetFormDTO;
import com.example.demo.models.dto.PetResponseDTO;
import com.example.demo.services.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Gerenciamento de pets dos usuários")
public class PetController {
    private final PetService petService;

    @PostMapping
    @Operation(summary = "Cadastrar pet", description = "Cadastra um novo pet no sistema vinculado a um dono")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pet cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<?> criar(@RequestBody PetFormDTO dto) {
        try {
            return ResponseEntity.status(201).body(petService.criar(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar pets", description = "Retorna todos os pets ou filtra por dono específico")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<PetResponseDTO>> listar(@RequestParam(required = false) Long donoId) {
        return ResponseEntity.ok(petService.listarPets(donoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pet por ID", description = "Retorna as informações de um pet específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(petService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pet", description = "Atualiza os dados de um pet existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PetFormDTO dto) {
        try {
            return ResponseEntity.ok(petService.atualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pet", description = "Remove um pet do sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pet removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            petService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}