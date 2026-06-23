package com.example.demo.controllers;

import com.example.demo.models.dto.LoginDTO;
import com.example.demo.models.dto.TokenDTO;
import com.example.demo.models.entities.Usuario;
import com.example.demo.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas a autenticação de usuários")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Efetua a autenticação do usuário com email e senha e retorna um token JWT de acesso.")
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody @Valid LoginDTO dados) {
        Usuario usuario = usuarioRepository.findByEmail(dados.email())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos!"));
        if (!passwordEncoder.matches(dados.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos!");
        }
        var tokenJWT = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(new TokenDTO(tokenJWT));
    }
}
