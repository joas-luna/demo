package com.example.demo.services;


import com.example.demo.models.dto.UsuarioDTO;
import com.example.demo.models.entities.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Usuario criaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(
                usuarioDTO.getNome(),
                usuarioDTO.getEmail(),
                usuarioDTO.getSenha(),
                usuarioDTO.getPais()
        );
        usuarioRepository.save(usuario);
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        } else {
            return usuarioOptional.get();
        }
    }

//    public Usuario atualizar(Long id, Usuario dadosAtualizacao) {}

    public void remover(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        } else {
            usuarioRepository.deleteById(id);
        }
    }

    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        if(usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(buscarPorId(id).getEmail())) {
            buscarPorId(id).setEmail(usuarioDTO.getEmail());
        }
        if(usuarioDTO.getNome() != null && !usuarioDTO.getNome().equals(buscarPorId(id).getNome())) {
            buscarPorId(id).setNome(usuarioDTO.getNome());
        }
        if(usuarioDTO.getPais() != null && !usuarioDTO.getPais().equals(buscarPorId(id).getPais())) {
            buscarPorId(id).setPais(usuarioDTO.getPais());
        }
        if(usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().equals(buscarPorId(id).getSenha())) {
            buscarPorId(id).setSenha(usuarioDTO.getSenha());
        }
        usuarioRepository.save(buscarPorId(id));
        return buscarPorId(id);
    }
}
