package com.example.demo.controllers;

import com.example.demo.models.dto.ConsultaDTO;
import com.example.demo.models.dto.ConsultaFormDTO;
import com.example.demo.services.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {
    private final ConsultaService consultaService;

    @PostMapping
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
    public ResponseEntity<?> listar(@RequestParam(required = false) Long petId) {
        List<ConsultaDTO> consultas = petId != null
                ? consultaService.listarPorPet(petId)
                : consultaService.listarTodas();
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(consultaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            consultaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
