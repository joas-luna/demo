package com.example.demo.user;

import com.example.demo.user.model.dto.requests.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsereController {
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
    public ResponseEntity<?> criarUsuario(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.post(userRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuarioPorId(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        return userService.put(id, userRequestDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcialmenteUsuarioPorId(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
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
