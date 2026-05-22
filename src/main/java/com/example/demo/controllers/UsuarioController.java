package com.example.demo.controllers;

import com.example.demo.models.dto.UsuarioDTO;
import com.example.demo.models.entities.Usuario;
import com.example.demo.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> criaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.status(201).body(usuarioService.criaUsuario(usuarioDTO));
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> encontraUsuario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.obtemUsuarioPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaUsuario(@PathVariable Long id) {
        try {
            usuarioService.removeUsuarioPorId(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaUsuario(@PathVariable Long id,
                                              @RequestBody UsuarioDTO usuarioDTO) {
        try {
            return ResponseEntity.ok(usuarioService.atualizaUsuario(id, usuarioDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}