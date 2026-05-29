package com.example.demo.services;

import com.example.demo.models.dto.VeterinarioDTO;
import com.example.demo.models.entities.Veterinario;
import com.example.demo.repositories.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinarioService {
    private final VeterinarioRepository veterinarioRepository;

    public Veterinario encontrarPorId(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario de id " + id + " nao encontrado"));
    }

    public Veterinario cadastrar(VeterinarioDTO veterinarioDTO) {
        Veterinario veterinario = new Veterinario(veterinarioDTO.nome(), veterinarioDTO.crmv());
        return veterinarioRepository.save(veterinario);
    }

    public List<Veterinario> listarTodos() {
        return veterinarioRepository.findAll();
    }

    public Veterinario atualizar(Long id, VeterinarioDTO dto) {
        Veterinario veterinario = encontrarPorId(id);
        if (dto.nome() != null) {
            veterinario.setNome(dto.nome());
        }
        if (dto.crmv() != null) {
            veterinario.setCrmv(dto.crmv());
        }
        return veterinarioRepository.save(veterinario);
    }

    public void deletar(Long id) {
        Veterinario veterinario = encontrarPorId(id);
        veterinarioRepository.delete(veterinario);
    }


}
