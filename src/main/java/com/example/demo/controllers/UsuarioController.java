package com.example.demo.controllers;

import com.example.demo.models.dto.UsuarioFormDTO;
import com.example.demo.services.UsuarioService;
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

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name= "Usuários", description = "Operações relacionadas a usuários (tutores) da clínica")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary="Criar um novo usuário", description = "Cria um novo usuário (tutor) na clínica. O corpo da requisição deve conter os dados do usuário, como nome, email e telefone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class)))
    })
   public ResponseEntity<?> criaUsuario(@RequestBody @Valid UsuarioFormDTO usuarioDTO) {
        return ResponseEntity.status(201).body(usuarioService.criaUsuario(usuarioDTO));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna todos os usuarios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios retornada com sucesso")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Retorna um usuario pelo identificador informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> encontraUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtemUsuarioPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuario", description = "Remove um usuario pelo identificador informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deletaUsuario(@PathVariable Long id) {
        usuarioService.removeUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Atualiza os dados de um usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> atualizaUsuario(@PathVariable Long id,
            @RequestBody @Valid UsuarioFormDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.atualizaUsuario(id, usuarioDTO));
    }
}