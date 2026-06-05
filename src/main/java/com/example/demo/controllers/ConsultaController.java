package com.example.demo.controllers;

import com.example.demo.models.dto.ConsultaDTO;
import com.example.demo.models.dto.ConsultaFormDTO;
import com.example.demo.services.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Gerenciamento de consultas veterinárias")
public class ConsultaController {
    private final ConsultaService consultaService;

    @PostMapping
    @Operation(summary = "Agendar uma consulta", description = "Agenda uma nova consulta para um pet com um veterinário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário ou regra de negócio violada"),
            @ApiResponse(responseCode = "404", description = "Pet ou Veterinário não encontrado")
    })
    public ResponseEntity<?> criar(@RequestBody ConsultaFormDTO dto) {
        try {
            return new ResponseEntity<>(consultaService.cria(dto), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // /consultas?vet_id=3&data=2026-05-29
    @GetMapping
    @Operation(summary = "Listar consultas", description = "Lista todas as consultas ou filtra por ID do pet")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<?> listar(@RequestParam(required = false) Long petId) {
        List<ConsultaDTO> consultas = petId != null
                ? consultaService.listarPorPet(petId)
                : consultaService.listarTodas();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna os detalhes de uma consulta específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(consultaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma consulta", description = "Atualiza os dados de uma consulta existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta atualizada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário ou regra de negócio violada"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ConsultaFormDTO dto) {
        try {
            return ResponseEntity.ok(consultaService.atualizar(id, dto));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar uma consulta", description = "Cancela/remove uma consulta do sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            consultaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
