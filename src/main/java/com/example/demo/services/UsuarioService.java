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

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Usuario criaUsuario(UsuarioFormDTO usuarioDTO) {
        Usuario usuario = new Usuario(
                usuarioDTO.nome(),
                usuarioDTO.email(),
                usuarioDTO.senha(),
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
            throw new RuntimeException(mensagem);
        }
        log.info("Usuário de id {} encontrado", id);
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

    public Usuario atualizaUsuario(Long id, UsuarioDTO dadosParaAtualizar) {
        Usuario usuarioExistente = encontraUsuarioPorId(id);
        if (dadosParaAtualizar.nome() != null) usuarioExistente.setNome(dadosParaAtualizar.nome());
        if (dadosParaAtualizar.email() != null) usuarioExistente.setEmail(dadosParaAtualizar.email());
        if (dadosParaAtualizar.senha() != null) usuarioExistente.setSenha(dadosParaAtualizar.senha());
        if (dadosParaAtualizar.pais() != null) usuarioExistente.setPais(dadosParaAtualizar.pais());
        usuarioRepository.save(usuarioExistente);
        log.info("Usuário de id {} atualizado com sucesso", id);
        return usuarioExistente;
    }
}
