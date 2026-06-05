package com.example.demo.controllers;

import com.example.demo.models.dto.VeterinarioDTO;
import com.example.demo.services.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veterinarios")
@RequiredArgsConstructor
@Tag(name = "Veterinários", description = "Gerenciamento dos médicos veterinários da clínica")
public class VeterinarioController {
    private final VeterinarioService veterinarioService;

    @PostMapping
    @Operation(summary = "Cadastrar veterinário", description = "Registra um novo veterinário com seu respectivo CRMV")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veterinário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<?> criar(@RequestBody VeterinarioDTO dto) {
        try {
            return new ResponseEntity<>(veterinarioService.cadastrar(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Listar veterinários", description = "Retorna todos os veterinários cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(veterinarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veterinário por ID", description = "Busca os detalhes de um veterinário específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado")
    })
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(veterinarioService.encontrarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veterinário", description = "Atualiza os dados (como nome e CRMV) de um veterinário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veterinário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody VeterinarioDTO dto) {
        try {
            return ResponseEntity.ok(veterinarioService.atualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veterinário", description = "Remove um veterinário do sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veterinário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            veterinarioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
