package com.example.demo.controllers;


import com.example.demo.models.dto.UsuarioDTO;
import com.example.demo.models.entities.Usuario;
import com.example.demo.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> criaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioCriado = usuarioService.criaUsuario(usuarioDTO);
        return ResponseEntity.status(201).body(usuarioCriado);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.status(200).body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Long id) {
        return ResponseEntity.status(200).body(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarUsuario(
            @PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO
    )
    {
        usuarioService.buscarPorId(id).setEmail(usuarioDTO.getEmail());
        usuarioService.buscarPorId(id).setEmail(usuarioDTO.getNome());
        usuarioService.buscarPorId(id).setEmail(usuarioDTO.getSenha());
        usuarioService.buscarPorId(id).setEmail(usuarioDTO.getPais());

        return ResponseEntity.status(200).body(usuarioService.buscarPorId(id));
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<?> modificarParcialmenteUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
//        return ResponseEntity.status(200).body();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarUsuario(@PathVariable Long id) {
        usuarioService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
