package com.example.demo.services;

import com.example.demo.models.dto.EnderecoDTO;
import com.example.demo.models.entities.Endereco;
import com.example.demo.models.entities.Usuario;
import com.example.demo.repositories.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.exceptions.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioService usuarioService;

    public Endereco criar(Long usuarioId, EnderecoDTO dto) {
        if (enderecoRepository.existsByUsuarioId(usuarioId)) {
            throw new IllegalArgumentException("Usuario ja possui um endereco cadastrado");
        }
        Usuario usuario = usuarioService.encontraUsuarioPorId(usuarioId);
        Endereco endereco = new Endereco(dto.rua(), dto.numero(), dto.bairro(),
                dto.cidade(), dto.estado(), dto.cep(), usuario);
        return enderecoRepository.save(endereco);
    }

    public Endereco buscarPorUsuario(Long usuarioId) {
        usuarioService.obtemUsuarioPorId(usuarioId);
        return enderecoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endereco nao encontrado para o usuario " + usuarioId));
    }

    public Endereco atualizar(Long usuarioId, EnderecoDTO dto) {
        Endereco endereco = buscarPorUsuario(usuarioId);
        endereco.setRua(dto.rua());
        endereco.setNumero(dto.numero());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
        return enderecoRepository.save(endereco);
    }

    @Transactional
    public void deletar(Long usuarioId) {
        Endereco endereco = buscarPorUsuario(usuarioId);
        enderecoRepository.deleteById(endereco.getId());
    }
}
