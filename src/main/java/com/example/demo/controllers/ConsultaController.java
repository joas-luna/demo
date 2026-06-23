package com.example.demo.controllers;

import com.example.demo.models.dto.ConsultaDTO;
import com.example.demo.models.dto.ConsultaFormDTO;
import com.example.demo.services.ConsultaService;
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

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Operacoes de consultas veterinarias")
public class ConsultaController {
    private final ConsultaService consultaService;

    @PostMapping
    @Operation(summary = "Criar consulta", description = "Cria uma consulta para um pet e veterinario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consulta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pet ou veterinario nao encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de agendamento", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> criar(@RequestBody @Valid ConsultaFormDTO dto) {
        return new ResponseEntity<>(consultaService.cria(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar consultas", description = "Lista todas as consultas ou filtra por petId")
    @ApiResponse(responseCode = "200", description = "Consultas retornadas com sucesso")
    public ResponseEntity<?> listar(@RequestParam(required = false) Long petId) {
        List<ConsultaDTO> consultas = petId != null
                ? consultaService.listarPorPet(petId)
                : consultaService.listarTodas();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resumo de consultas", description = "Retorna um resumo com estatisticas das consultas")
    @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    public ResponseEntity<?> obterResumo() {
        return ResponseEntity.ok(consultaService.obterResumo());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna uma consulta pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
            @ApiResponse(responseCode = "404", description = "Consulta nao encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar consulta", description = "Atualiza os dados de uma consulta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Consulta, pet ou veterinario nao encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de agendamento", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid ConsultaFormDTO dto) {
        return ResponseEntity.ok(consultaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover consulta", description = "Remove uma consulta pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consulta removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta nao encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        consultaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
