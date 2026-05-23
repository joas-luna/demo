package com.example.demo.user.controller;

import com.example.demo.user.dto.user.request.UserRequestDTO;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody List<UserRequestDTO> userRequestDTOList) {
        return userService.post(userRequestDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuarioPorId(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.put(id, userRequestDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcialmenteUsuarioPorId(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.patch(id, userRequestDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deletarTodosOsUsuarios() {
        return userService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuarioPorId(@PathVariable Long id) {
        return userService.deleteById(id);
    }
}
