package com.example.demo.controllers;

import com.example.demo.models.dto.EnderecoDTO;
import com.example.demo.models.entities.Endereco;
import com.example.demo.services.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{usuarioId}/endereco")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<?> criar(@PathVariable Long usuarioId,
                                   @RequestBody EnderecoDTO dto) {
        try {
            Endereco endereco = enderecoService.criar(usuarioId, dto);
            return ResponseEntity.status(201).body(endereco);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> buscar(@PathVariable Long usuarioId) {
        try {
            Endereco endereco = enderecoService.buscarPorUsuario(usuarioId);
            return ResponseEntity.ok(endereco);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@PathVariable Long usuarioId,
                                       @RequestBody EnderecoDTO dto) {
        try {
            Endereco endereco = enderecoService.atualizar(usuarioId, dto);
            return ResponseEntity.ok(endereco);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletar(@PathVariable Long usuarioId) {
        try {
            enderecoService.deletar(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

