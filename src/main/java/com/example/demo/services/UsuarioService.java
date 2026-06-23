package com.example.demo.services;

import com.example.demo.models.dto.UsuarioDTO;
import com.example.demo.models.dto.UsuarioFormDTO;
import com.example.demo.models.entities.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.exceptions.ResourceNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    // PasswordEncoder injetado para codificar senhas de forma segura (Adicionado)
    private final PasswordEncoder passwordEncoder;

    public boolean usuarioComEsteEmailJaExiste(String email) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findByEmail(email);
        return possivelUsuario.isPresent();
    }

    public Usuario criaUsuario(UsuarioFormDTO usuarioDTO) {
        if (usuarioComEsteEmailJaExiste(usuarioDTO.email())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com o email " + usuarioDTO.email());
        }
        // Cria o usuário com a senha já criptografada via BCrypt (Alterado)
        Usuario usuario = new Usuario(
                usuarioDTO.nome(),
                usuarioDTO.email(),
                // Codifica/criptografa a senha antes de salvar no banco (Alterado)
                passwordEncoder.encode(usuarioDTO.senha()),
                usuarioDTO.pais());
        usuarioRepository.save(usuario);
        log.info("Usuário de id {} criado com sucesso", usuario.getId());
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario encontraUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            String mensagem = "Usuário de id " + id + " não encontrado";
            log.error(mensagem);
            throw new ResourceNotFoundException(mensagem);
        }
        log.info("Usuário de id {} encontrado", id);
        return usuarioOpt.get();
    }

    public Usuario encontraUsuarioPorEmail(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            String mensagem = "Usuário de email " + email + " não encontrado";
            log.error(mensagem);
            throw new ResourceNotFoundException(mensagem);
        }
        log.info("Usuário de email {} encontrado", email);
        return usuarioOpt.get();
    }

    public UsuarioDTO obtemUsuarioPorId(Long id) {
        return encontraUsuarioPorId(id).toDTO();
    }

    public void removeUsuarioPorId(Long id) {
        Usuario usuario = encontraUsuarioPorId(id);
        usuarioRepository.delete(usuario);
        log.info("Usuário de id {} removido com sucesso", id);
    }

    public Usuario atualizaUsuario(Long id, UsuarioFormDTO dadosParaAtualizar) {
        Usuario usuarioExistente = encontraUsuarioPorId(id);
        if (dadosParaAtualizar.nome() != null)
            usuarioExistente.setNome(dadosParaAtualizar.nome());
        if (dadosParaAtualizar.email() != null)
            usuarioExistente.setEmail(dadosParaAtualizar.email());
        if (dadosParaAtualizar.senha() != null)
            // Criptografa a nova senha antes de atualizar (Alterado)
            usuarioExistente.setSenha(passwordEncoder.encode(dadosParaAtualizar.senha()));
        if (dadosParaAtualizar.pais() != null)
            usuarioExistente.setPais(dadosParaAtualizar.pais());
        usuarioRepository.save(usuarioExistente);
        log.info("Usuário de id {} atualizado com sucesso", id);
        return usuarioExistente;
    }
}
